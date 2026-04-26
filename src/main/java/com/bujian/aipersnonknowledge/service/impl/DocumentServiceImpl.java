package com.bujian.aipersnonknowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.mapper.DocumentMapper;
import com.bujian.aipersnonknowledge.service.DocumentService;
import com.bujian.aipersnonknowledge.vo.DocumentVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {
    @Override
    public Document createDocument(DocumentVo documentVo) {
        Document document = new Document();
        document.setTitle( documentVo.getTitle());
        document.setContent( documentVo.getContent());
        document.setDescription( documentVo.getDescription());
        document.setBaseId(documentVo.getBaseId());
        document.setUserId(documentVo.getUserId());
        // 保存文档
        boolean saved = this.saveOrUpdate(document);
        if (!saved) {
            throw new RuntimeException("保存文档失败");
        }
        return document;
    }


    @Override
    public List<Document> getDocumentTreeByKnowledgeBaseId(String knowledgeBaseId) {
        Thread Thead = new Thread();
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Document::getBaseId, knowledgeBaseId)
                .orderByAsc(Document::getCreateTime);
        List<Document> allDocuments = this.list(queryWrapper);
        // 构建文档树
        return buildDocumentTree(allDocuments, "0");
    }

    @Override
    public Long getCount(String knowledgeBaseId) {
        return this.count(new LambdaQueryWrapper<Document>().eq(Document::getBaseId, knowledgeBaseId));
    }

    /**
     * 递归构建文档树
     */
    private List<Document> buildDocumentTree(List<Document> documents, String parentId) {
        return documents.stream()
                .filter(doc -> Objects.equals(doc.getBaseId(), parentId))
                .collect(Collectors.toList());
    }
}

