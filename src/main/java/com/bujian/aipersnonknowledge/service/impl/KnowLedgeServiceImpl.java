package com.bujian.aipersnonknowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import com.bujian.aipersnonknowledge.mapper.KnowledgeBasesMapper;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<KnowledgeBases> searchKnowledgeBasesByUserId(Integer userId, String keyword) {
        return baseMapper.searchKnowledgeBasesByUserId(userId, keyword);
    }
}
