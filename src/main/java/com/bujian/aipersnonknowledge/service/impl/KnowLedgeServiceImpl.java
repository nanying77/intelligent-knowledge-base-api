package com.bujian.aipersnonknowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import com.bujian.aipersnonknowledge.mapper.KnowledgeBasesMapper;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import com.bujian.aipersnonknowledge.vo.SearchKnowledgeResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class KnowLedgeServiceImpl extends ServiceImpl<KnowledgeBasesMapper, KnowledgeBases> implements KnowledgeService {
    @Override
    public List<KnowledgeBases> getKnowledgeBasesByUserId(Integer userId) {
        QueryWrapper<KnowledgeBases> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<SearchKnowledgeResultVO> searchKnowledgeBasesByUserId(Integer userId, String keyword) {
        // 查询原始结果 - 返回扁平化的Map列表
        List<Map<String, Object>> rawResults = baseMapper.searchKnowledgeBasesByUserId(userId, keyword);
        
        log.info("搜索关键词: {}, 原始查询结果数: {}", keyword, rawResults.size());
        for (int i = 0; i < rawResults.size(); i++) {
            Map<String, Object> row = rawResults.get(i);
            log.info("原始结果[{}]: kb_id={}, kb_name={}, document_id={}, document_title={}, matched_field={}",
                i,
                row.get("kb_id"),
                row.get("kb_name"),
                row.get("document_id"),
                row.get("document_title"),
                row.get("matched_field")
            );
        }
        
        // 按知识库ID聚合，将同一知识库的多个文档合并
        Map<String, SearchKnowledgeResultVO> aggregatedMap = new LinkedHashMap<>();
        
        for (Map<String, Object> row : rawResults) {
            String kbId = (String) row.get("kb_id");
            
            if (!aggregatedMap.containsKey(kbId)) {
                // 第一次出现该知识库，创建新条目
                SearchKnowledgeResultVO aggregated = new SearchKnowledgeResultVO();
                
                // 构建知识库对象
                KnowledgeBases kb = new KnowledgeBases();
                kb.setId(kbId);
                kb.setUserId((String) row.get("kb_user_id"));
                kb.setName((String) row.get("kb_name"));
                kb.setDescription((String) row.get("kb_description"));
                kb.setCreateAt((String) row.get("kb_create_at"));
                kb.setCreateTime(row.get("kb_create_time") != null ? 
                    (java.time.LocalDateTime) row.get("kb_create_time") : null);
                
                aggregated.setKnowledgeBase(kb);
                aggregated.setMatchedField((String) row.get("matched_field"));
                aggregated.setMatchedDocuments(new ArrayList<>());
                aggregatedMap.put(kbId, aggregated);
                log.info("创建新知识库聚合项: kbId={}, kbName={}", kbId, kb.getName());
            }
            
            // 添加匹配的文档信息
            SearchKnowledgeResultVO existing = aggregatedMap.get(kbId);
            if (row.get("document_id") != null) {
                SearchKnowledgeResultVO.DocumentMatchInfo docInfo = new SearchKnowledgeResultVO.DocumentMatchInfo();
                docInfo.setDocumentId((Integer) row.get("document_id"));
                docInfo.setTitle((String) row.get("document_title"));
                docInfo.setContent((String) row.get("document_content"));
                docInfo.setBaseId((String) row.get("document_base_id"));
                docInfo.setCreateTime(row.get("document_create_time") != null ? 
                    row.get("document_create_time").toString() : null);
                docInfo.setMatchType((String) row.get("matched_field"));
                
                existing.getMatchedDocuments().add(docInfo);
                log.info("添加文档到知识库 {}: 文档ID={}, 文档标题={}", 
                    kbId, 
                    docInfo.getDocumentId(),
                    docInfo.getTitle()
                );
            }
        }
        
        List<SearchKnowledgeResultVO> finalResults = new ArrayList<>(aggregatedMap.values());
        log.info("聚合后结果数: {}", finalResults.size());
        for (int i = 0; i < finalResults.size(); i++) {
            SearchKnowledgeResultVO result = finalResults.get(i);
            log.info("最终结果[{}]: 知识库ID={}, 知识库名称={}, 匹配文档数={}",
                i,
                result.getKnowledgeBase().getId(),
                result.getKnowledgeBase().getName(),
                result.getMatchedDocuments() != null ? result.getMatchedDocuments().size() : 0
            );
        }
        
        // 返回聚合后的结果
        return finalResults;
    }

}
