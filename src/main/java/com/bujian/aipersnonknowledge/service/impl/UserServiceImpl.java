package com.bujian.aipersnonknowledge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bujian.aipersnonknowledge.entity.User;
import com.bujian.aipersnonknowledge.mapper.UserMapper;
import com.bujian.aipersnonknowledge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
