package com.bujian.aipersnonknowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 知识库表 Mapper 接口
 * </p>
 * @author MuQin
 */
@Mapper
public interface KnowledgeBasesMapper extends BaseMapper<KnowledgeBases> {

    List<KnowledgeBases> searchKnowledgeBasesByUserId(  @Param("userId") Integer userId,
                                                        @Param("keyword") String keyword);
}