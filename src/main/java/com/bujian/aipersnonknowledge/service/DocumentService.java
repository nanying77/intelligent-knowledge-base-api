package com.bujian.aipersnonknowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bujian.aipersnonknowledge.dto.DocumentCreateDTO;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import com.bujian.aipersnonknowledge.vo.DocumentVo;

import java.util.List;

public interface DocumentService  extends IService<Document> {

    Document createDocument(DocumentVo documentVo);

//    List<KnowledgeBases> getUserKnowledgeBases(Integer userId);

    List<Document> getDocumentTreeByKnowledgeBaseId(String knowledgeBaseId);





}
