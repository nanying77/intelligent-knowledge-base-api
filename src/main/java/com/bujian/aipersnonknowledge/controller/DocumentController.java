package com.bujian.aipersnonknowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.service.DocumentService;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import com.bujian.aipersnonknowledge.vo.DocumentVo;
import com.bujian.aipersnonknowledge.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
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

     @Operation(summary = "新建文档")
    @PostMapping("/create")
    public Result createDocument(@Valid @RequestBody DocumentVo documentVo) {
        try {
            // 验证知识库ID不能为空
            if (documentVo.getBaseId() == null) {
                return Result.error("请选择知识库");
            }
            //校验文档名称是否已经存在
            LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(Document::getBaseId, documentVo.getBaseId())
                    .eq(Document::getTitle, documentVo.getTitle())
                    .eq(Document::getContent, documentVo.getContent());
            if (documentService.count(queryWrapper) > 1) {
                return Result.error("文档名称已存在");
            }
            Document document = new Document();
            BeanUtils.copyProperties(documentVo, document);
            // 调用服务层创建文档
         documentService.saveOrUpdate(document);
            Map<String, Object> result = new HashMap<>();
            result.put("base_id", document.getBaseId());
            result.put("documentId", document.getId());
            result.put("title", document.getTitle());
            result.put("message", "创建成功");
            log.info("文档创建成功, ID: {}, 标题: {}", document.getId(), document.getTitle());
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
        boolean delete = documentService.removeById(id);
        return delete ? Result.success("删除成功") : Result.error("删除失败");
    }

     @Operation(summary = "文档列表")
    @GetMapping("/list")
    public Result list(@NotNull(message = "知识库不存在，请检查") @RequestParam String baseId) {
        LambdaQueryWrapper<Document> DocumentWrapper = new LambdaQueryWrapper<>();
        DocumentWrapper.eq(Document::getBaseId, baseId)
                .orderByDesc(Document::getCreateTime);
        List<Document> documents = documentService.list(DocumentWrapper);
        return Result.success(documents);
    }

     @Operation(summary = "收藏文档")
    @GetMapping("/favorite")
    public Result favorite(@NotNull(message = "文档为空，请检查") @RequestParam Integer id) {
        boolean update = documentService.lambdaUpdate()
                .set(Document::getIsFavorite, true)
                .eq(Document::getId, id)
                .update();
        return update ? Result.success("收藏成功") : Result.error("收藏失败");
    }

     @Operation(summary = "取消收藏文档")
    @GetMapping("/unfavorite")
    public Result cancelFavorite(@NotNull(message = "文档为空，请检查") @RequestParam Integer id) {
        boolean update = documentService.lambdaUpdate()
                .set(Document::getIsFavorite, false)
                .eq(Document::getId, id)
                .update();
        return update ? Result.success("取消收藏成功") : Result.error("取消收藏失败");
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
}