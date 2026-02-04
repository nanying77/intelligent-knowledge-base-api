package com.bujian.aipersnonknowledge.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.service.DocumentService;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import com.bujian.aipersnonknowledge.vo.DocumentVo;
import com.bujian.aipersnonknowledge.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文档管理控制器
 */
@Slf4j
@Tag(name="文档管理")
@RestController
@RequestMapping("base/document")
@RequiredArgsConstructor
public class DocumentController {
    @Autowired
    private RedisTemplate redisTemplate;
    private final DocumentService documentService;
    private final KnowledgeService knowledgeService;
//    @Operation(summary = "新建文档页面")
//    @GetMapping("/page")
//    public Result page(@RequestParam Integer userId) {
//
//        List<KnowledgeBases> knowledgeBases = knowledgeService.getKnowledgeBasesByUserId(userId);
//        try {
//            Map<String, Object> result = new HashMap<>();
//            result.put("knowledgeBases", knowledgeBases);
//            result.put("userId", userId);
//            return Result.success(result);
//        } catch (Exception e) {
//            log.error("获取新建文档页面数据失败, 用户ID: {}", userId, e);
//            return Result.error("获取数据失败");
//        }
//        }
    @Operation(summary = "新建文档")
    @PostMapping("/create")
    public Result createDocument(@Valid @RequestBody DocumentVo documentVo) {
        try {
            // 验证知识库ID不能为空
            if (documentVo.getBaseId() == null) {
                return Result.error("请选择知识库");
            }
            // 调用服务层创建文档
            Document document = documentService.createDocument(documentVo);

            Map<String, Object> result = new HashMap<>();
            result.put("base_id", document.getBaseId());
            result.put("documentId", document.getId());
            result.put("title", document.getTitle());
            result.put("message", "创建成功");
            log.info("文档创建成功, ID: {}, 标题: {}", document.getId(), document.getTitle());
            return Result.success(result);
        } catch (Exception e) {
            log.error("创建文档失败, 用户ID: {}, 知识库ID: {}",
                    documentVo.getUserId(), documentVo.getBaseId(), e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }
    @Operation(summary = "删除文档")
    @DeleteMapping("/delete")
    public Result deleteDocument(@NotNull(message="文档为空，请检查") @RequestParam Integer id) {
        boolean delete = documentService.removeById(id);
        return delete? Result.success("删除成功"):Result.error("删除失败");
    }

    @Operation(summary = "查看文档")
    @GetMapping("/list")
    public Result listDocument(@NotNull(message = "知识库不存在，请检查") @RequestParam Integer baseId) {
        LambdaQueryWrapper <Document> DocumentWrapper = new LambdaQueryWrapper<>();
        DocumentWrapper.eq(Document::getBaseId, baseId)
                .orderByDesc(Document::getCreateTime);
        List<Document> documents = documentService.list(DocumentWrapper);
        return Result.success(documents);
    }
}