package com.bujian.aipersnonknowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import com.bujian.aipersnonknowledge.entity.User;
import com.bujian.aipersnonknowledge.service.DocumentService;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import com.bujian.aipersnonknowledge.service.UserService;
import com.bujian.aipersnonknowledge.vo.Result;
import com.bujian.aipersnonknowledge.vo.SearchKnowledgeResultVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@RequestMapping("base/knowledge")
@RestController
@Slf4j
@RequiredArgsConstructor

/**
 * 知识库控制器
 */
public class KnowledgeController {

    private final RedisTemplate redisTemplate;
    private final KnowledgeService knowledgeService;
    private final UserService userService;
    private final DocumentService documentService;

   @Operation(summary= "知识库列表")
    /**
     * 获取知识库列表
     */
    @GetMapping("/list")
    public Result<List<KnowledgeBases>> list(@RequestParam int userId) {
        try {
            String redisKey = "user_knowledge:" + userId;

            LambdaQueryWrapper<KnowledgeBases> knowledgeBasesLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 如果是管理员，则返回所有知识库
            Integer role = userService.getById(userId).getRole();
            if (role != null && role != 1) {
                knowledgeBasesLambdaQueryWrapper.eq(KnowledgeBases::getUserId, userId);
            }
            List<KnowledgeBases> klist = knowledgeService.list(knowledgeBasesLambdaQueryWrapper);
            klist.forEach(knowledge -> {
                Long count = documentService.getCount(knowledge.getId());
                knowledge.setDocumentCount(count);
            });
            klist.sort(Comparator.comparing(KnowledgeBases::getId));

            redisTemplate.opsForValue().set(redisKey, klist, Duration.ofHours(2));

            return Result.success(klist);
        } catch (Exception e) {
            log.error("获取失败:{}", e.getMessage());
            return Result.error("获取知识库列表失败");
        }
    }

    /**
     * 创建知识库
     */
    @PostMapping("/create")
   @Operation(summary= "创建知识库")
    public Result<KnowledgeBases> create(@RequestBody KnowledgeBases knowledge) {
        try {
            // 检查知识库名称是否重复
            boolean exists = knowledgeService.lambdaQuery()
                    .eq(KnowledgeBases::getName, knowledge.getName())
                    .eq(KnowledgeBases::getUserId, knowledge.getUserId())
                    .exists();
            if (exists) {
                return Result.error("创建失败,已存在同名知识库");
            }
            User user = userService.getById(knowledge.getUserId());
            knowledge.setCreateAt(user.getUsername());
            // 保存知识库
            boolean saved = knowledgeService.save(knowledge);
            if (!saved) {
                return Result.error("添加失败");
            }
            // 清理缓存
            String redisKey = "user_knowledge:" + knowledge.getUserId();
            try {
                redisTemplate.delete(redisKey);
            } catch (Exception e) {
                log.warn("清理缓存失败: {}", e.getMessage());
            }
            return Result.success("创建成功");
        } catch (Exception e) {
            log.error("创建知识库失败: {}", e.getMessage(), e);
            return Result.error("添加失败,系统异常");
        }
    }

    /**
     * 删除知识库
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam String id) {
        try {
            KnowledgeBases knowledgeVo = knowledgeService.getById(id);
            String redisKey = "user_knowledge:" + knowledgeVo.getUserId();
            boolean b = knowledgeService.removeById(id);
            if (!b) {
                return Result.error("删除失败");
            }
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除失败请检查:{}", e.getMessage());
            return Result.error("删除失败，系统异常");
        }
    }

    /**
     * 搜索知识库,文档
     */
   @Operation(summary= "搜索知识库或文档内容")
    @GetMapping("search")
    public Result<List<SearchKnowledgeResultVO>> searchKnowledgeBases(@RequestParam Integer userId,
                                       @RequestParam String keyword)
   {
        try {
            List<SearchKnowledgeResultVO> searchResults = knowledgeService
                    .searchKnowledgeBasesByUserId(userId, keyword);
            return Result.success(searchResults);
        } catch (Exception e) {
            return Result.error("搜索失败");
        }
}

   @Operation(summary= "获取知识库文档树")
    @GetMapping("/document/tree")
    public Result<KnowledgeBases> documentTree(@RequestParam String baseId) {
        KnowledgeBases knowledgeBase = knowledgeService.getById(baseId);
        if (knowledgeBase == null) {
            return Result.error("知识库不存在");
        }
        List<Document> documents = documentService.getDocumentTreeByKnowledgeBaseId(baseId);
        knowledgeBase.setDocuments(documents);

        return Result.success(knowledgeBase);
    }
}