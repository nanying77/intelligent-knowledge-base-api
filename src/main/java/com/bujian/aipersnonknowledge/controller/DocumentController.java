package com.bujian.aipersnonknowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.service.CollaborationInviteService;
import com.bujian.aipersnonknowledge.service.DocumentService;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import com.bujian.aipersnonknowledge.utils.JwtUtils;
import com.bujian.aipersnonknowledge.vo.DocumentVo;
import com.bujian.aipersnonknowledge.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 文档管理控制器
 */
@Slf4j
@RestController
@RequestMapping("base/document")
@RequiredArgsConstructor
public class DocumentController {
    @Autowired
    private RedisTemplate redisTemplate;
    private final DocumentService documentService;
    private final KnowledgeService knowledgeService;
    private final CollaborationInviteService collaborationInviteService;
    private final JwtUtils jwtUtils;
    private final com.bujian.aipersnonknowledge.mapper.UserMapper userMapper;

    @Operation(summary = "新建文档")
    @PostMapping("/create")
    public Result createDocument(@Valid @RequestBody DocumentVo documentVo) {
        try {
            // 验证知识库ID不能为空
            if (documentVo.getBaseId() == null) {
                return Result.error("请选择知识库");
            }
            Document document = new Document();
            BeanUtils.copyProperties(documentVo, document);
            
            //校验文档名称是否已经存在
            LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(Document::getBaseId, documentVo.getBaseId())
                    .eq(Document::getTitle, documentVo.getTitle())
                    .eq(Document::getContent, documentVo.getContent());
            if (documentService.count(queryWrapper) > 1) {
                return Result.error("文档名称已存在");
            }
            
            // 调用服务层创建文档
            documentService.saveOrUpdate(document);
            
            // 清除对应知识库的文档列表缓存
            String redisKey = "document_list:" + document.getBaseId();
            redisTemplate.delete(redisKey);
            log.info("清除文档列表缓存: baseId={}", document.getBaseId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("base_id", document.getBaseId());
            result.put("documentId", document.getId());
            result.put("title", document.getTitle());
            result.put("message", "创建成功");
            log.info("文档创建成功, ID: {}, 标题: {}, 知识库ID: {}", document.getId(), document.getTitle(), document.getBaseId());
            return Result.success("保存成功");
        } catch (Exception e) {
            log.error("创建文档失败, 用户ID: {}, 知识库ID: {}",
                    documentVo.getUserId(), documentVo.getBaseId(), e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除文档")
    @DeleteMapping("/delete")
    public Result deleteDocument(@NotNull(message = "文档为空，请检查") @RequestParam Long id) {
        try {
            // 先获取文档信息，用于确定所属知识库
            Document document = documentService.getById(id);
            if (document == null) {
                return Result.error("文档不存在");
            }
            
            boolean delete = documentService.removeById(id);
            if (delete) {
                // 清除对应知识库的文档列表缓存
                String redisKey = "document_list:" + document.getBaseId();
                redisTemplate.delete(redisKey);
                log.info("清除文档列表缓存: baseId={}", document.getBaseId());
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除文档失败: documentId={}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "文档列表")
    @GetMapping("/list")
    public Result list(@NotNull(message = "知识库不存在，请检查") @RequestParam String baseId) {
        try {
            // 构建Redis缓存键
            String redisKey = "document_list:" + baseId;
            
            // 尝试从Redis获取缓存数据
            List<Document> cachedDocuments = (List<Document>) redisTemplate.opsForValue().get(redisKey);
            if (cachedDocuments != null) {
                log.debug("从Redis缓存获取文档列表: baseId={}", baseId);
                return Result.success(cachedDocuments);
            }
            
            // 如果缓存中没有数据，则从数据库查询
            LambdaQueryWrapper<Document> DocumentWrapper = new LambdaQueryWrapper<>();
            // 直接使用baseId字段查询
            DocumentWrapper.eq(Document::getBaseId, baseId);
            DocumentWrapper.orderByDesc(Document::getCreateTime);
            List<Document> documents = documentService.list(DocumentWrapper);
            
            // 将查询结果存入Redis缓存，设置过期时间为2小时
            if (documents != null && !documents.isEmpty()) {
                redisTemplate.opsForValue().set(redisKey, documents, 2, TimeUnit.HOURS);
                log.info("文档列表已缓存到Redis: baseId={}, count={}", baseId, documents.size());
            }
            
            return Result.success(documents);
        } catch (Exception e) {
            log.error("获取文档列表失败: baseId={}", baseId, e);
            return Result.error("获取文档列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "收藏文档")
    @GetMapping("/favorite")
    public Result favorite(@NotNull(message = "文档为空，请检查") @RequestParam Integer id) {
        try {
            // 先获取文档信息，用于确定所属知识库
            Document document = documentService.getById(id);
            if (document == null) {
                return Result.error("文档不存在");
            }
            
            boolean update = documentService.lambdaUpdate()
                    .set(Document::getIsFavorite, true)
                    .eq(Document::getId, id)
                    .update();
            
            if (update) {
                // 清除对应知识库的文档列表缓存
                String redisKey = "document_list:" + document.getBaseId();
                redisTemplate.delete(redisKey);
                log.info("清除文档列表缓存: baseId={}", document.getBaseId());
                return Result.success("收藏成功");
            } else {
                return Result.error("收藏失败");
            }
        } catch (Exception e) {
            log.error("收藏文档失败: documentId={}", id, e);
            return Result.error("收藏失败: " + e.getMessage());
        }
    }

    @Operation(summary = "取消收藏文档")
    @GetMapping("/unfavorite")
    public Result unfavorite(@NotNull(message = "文档为空，请检查") @RequestParam Integer id) {
        try {
            // 先获取文档信息，用于确定所属知识库
            Document document = documentService.getById(id);
            if (document == null) {
                return Result.error("文档不存在");
            }
            
            boolean update = documentService.lambdaUpdate()
                    .set(Document::getIsFavorite, false)
                    .eq(Document::getId, id)
                    .update();
            
            if (update) {
                // 清除对应知识库的文档列表缓存
                String redisKey = "document_list:" + document.getBaseId();
                redisTemplate.delete(redisKey);
                log.info("清除文档列表缓存: baseId={}", document.getBaseId());
                return Result.success("取消收藏成功");
            } else {
                return Result.error("取消收藏失败");
            }
        } catch (Exception e) {
            log.error("取消收藏文档失败: documentId={}", id, e);
            return Result.error("取消收藏失败: " + e.getMessage());
        }
    }

    /**
     * 获取收藏的文档列表
     *
     * @param userId 用户ID
     * @return 收藏的文档列表
     */
    @Operation(summary = "收藏的文档列表")
    @GetMapping("/favorites")
    public Result favoriteList(@NotNull(message = "用户ID为空，请检查") @RequestParam Long userId) {
        List<Document> documents = documentService.lambdaQuery()
                .eq(Document::getUserId, userId)
                .eq(Document::getIsFavorite, true)
                .orderByDesc(Document::getCreateTime)
                .list();
        if (documents.isEmpty()) {
            return Result.error("当前用户未收藏过任何文档");
        }
        return Result.success(documents);
    }

    /**
     * 查看最近创建的文档(时间为7天内)
     */
    @Operation(summary = "最近创建的文档")
    @GetMapping("/recent")
    public Result recent(@NotNull(message = "用户ID为空，请检查") @RequestParam Long userId) {
        List<Document> documents = documentService.lambdaQuery()
                .eq(Document::getUserId, userId)
                .ge(Document::getCreateTime, DateUtils.addDays(new Date(), -7))
                .orderByDesc(Document::getCreateTime)
                .list();
        if (documents.isEmpty()) {

            return Result.error("您最近一周内未创建过新文档");
        }
        return Result.success(documents);
    }
    
    /**
     * 获取文档详情（支持协作者访问）
     */
    @Operation(summary = "获取文档详情")
    @GetMapping("/{id}")
    public Result<Document> getDocumentDetail(
            @PathVariable Integer id,
            HttpServletRequest request) {
        try {
            // 1. 获取当前用户ID
            Integer userId = jwtUtils.getCurrentUserId(request);
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            // 2. 查询文档
            Document document = documentService.getById(id);
            if (document == null) {
                return Result.error("文档不存在");
            }
            
            // 3. 检查权限：文档所有者或协作者
            boolean isOwner = document.getUserId().equals((long) userId);
            boolean isCollaborator = collaborationInviteService.hasAccessPermission(id, userId);
            
//            if (!isOwner && !isCollaborator) {
//                return Result.error(403, "您没有权限访问该文档");
//            }
            
            // 4. 返回文档详情（包含content）
            log.info("获取文档详情: documentId={}, userId={}, isOwner={}, isCollaborator={}", 
                    id, userId, isOwner, isCollaborator);
            return Result.success(document);
        } catch (Exception e) {
            log.error("获取文档详情失败: documentId={}", id, e);
            return Result.error("获取文档详情失败: " + e.getMessage());
        }
    }

}