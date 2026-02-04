package com.bujian.aipersnonknowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bujian.aipersnonknowledge.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 * @author MuQin
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}