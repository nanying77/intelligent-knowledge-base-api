package com.bujian.aipersnonknowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import com.bujian.aipersnonknowledge.vo.SearchKnowledgeResultVO;

import java.util.List;

public interface KnowledgeService extends IService<KnowledgeBases> {
    List<KnowledgeBases> getKnowledgeBasesByUserId(Integer userId);


    List<SearchKnowledgeResultVO> searchKnowledgeBasesByUserId(Integer userId, String keyword);


}
