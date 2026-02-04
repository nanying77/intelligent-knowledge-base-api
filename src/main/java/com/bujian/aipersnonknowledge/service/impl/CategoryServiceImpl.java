package com.bujian.aipersnonknowledge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bujian.aipersnonknowledge.entity.Category;
import com.bujian.aipersnonknowledge.mapper.CategoryMapper;
import com.bujian.aipersnonknowledge.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}