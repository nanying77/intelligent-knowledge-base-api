package com.bujian.aipersnonknowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.Category;
import com.bujian.aipersnonknowledge.service.CategoryService;
import com.bujian.aipersnonknowledge.vo.CategoryVo;
import com.bujian.aipersnonknowledge.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/base/categories")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final CategoryService categoryService;


    // 获取当前用户的所有分类
    @GetMapping("/list")
   @Operation(summary= "根据用户ID获取对应分类")
    public Result<List<CategoryVo>> getUserCategories(@RequestParam int userId) {
//        String redisKey = "user_categories:" + userId;
//        List<CategoryVo> cachedResult = (List<CategoryVo>) redisTemplate.opsForValue().get(redisKey);
//        if (cachedResult != null && !cachedResult.isEmpty()) {
//            return Result.success(cachedResult);
//        }
        // 查询用户个人分类
        LambdaQueryWrapper<Category> userQuery = new LambdaQueryWrapper<>();
        userQuery.isNull(Category::getUserId).or().eq(Category::getUserId, userId)  ;

        // 查询系统分类
        LambdaQueryWrapper<Category> systemQuery = new LambdaQueryWrapper<>();
        systemQuery.eq(Category::getIsSystem, 1);
        List<Category> userCategories = categoryService.list(userQuery);
        List<Category> systemCategories = categoryService.list(systemQuery);


        // 合并列表
        List<Category> allCategories = new ArrayList<>();
        allCategories.addAll(systemCategories);
        allCategories.addAll(userCategories);

        // 按创建时间排序
        allCategories.sort(Comparator.comparing(Category::getCreateTime).reversed());

        // 转换为 CategoryVo 列表
        List<CategoryVo> categoryVoList = allCategories.stream()
                .map(category -> {
                    CategoryVo categoryVo = new CategoryVo();
                    categoryVo.setId(category.getId());
                    categoryVo.setName(category.getName());
                    categoryVo.setIsSystem(category.getIsSystem());
                    categoryVo.setParentId(category.getParentId());
                    categoryVo.setIcon(category.getIcon());
                    return categoryVo;
                })
                .collect(Collectors.toList());

//        redisTemplate.opsForValue().set(redisKey, categoryVoList, Duration.ofHours(2));

        return Result.success(categoryVoList);

    }

    // 创建分类
    @PostMapping("/create")
   @Operation(summary= "创建分类")
    public Result<Category> createCategory(@RequestBody CategoryVo categoryVO) {
        try {
            // 检查分类名称是否重复
            boolean exists = categoryService.lambdaQuery()
                    .eq(Category::getName, categoryVO.getName())
                    .eq(Category::getUserId, categoryVO.getUserId())
                    .exists();
            if (exists) {
                return Result.error("分类名称已存在，请更换其他名称");
            }

            // 创建分类实体
            Category category = new Category();
            category.setName(categoryVO.getName());
            category.setParentId(4);
            category.setCreateTime(LocalDateTime.now());
            category.setUpdateTime(LocalDateTime.now());
            category.setUserId(categoryVO.getUserId());
            category.setIcon(categoryVO.getIcon());
            category.setSortOrder(categoryVO.getSortOrder());
            category.setIsSystem(false);

            // 保存分类
            boolean save = categoryService.save(category);

            if (save) {
                // 清除用户分类缓存，让下次查询重新加载最新数据
                String redisKey = "user_categories:" + category.getUserId();
                redisTemplate.delete(redisKey);

                log.info("创建分类成功，已清除用户id {} 的分类缓存", category.getUserId());
                return Result.success("创建成功");
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建分类失败: {}", e.getMessage(), e);
            return Result.error("系统错误，请联系管理员");
        }
    }

    // 删除分类
    @DeleteMapping("/delete")
   @Operation(summary= "删除分类")
    public Result<Void> deleteCategory(@RequestParam int id) {
        try {
            Category category = categoryService.getById(id);
            String redisKey = "user_categories:" + category.getUserId();
            boolean delete = categoryService.removeById(id);
            if (delete) {
                redisTemplate.delete(redisKey);
            }
            return delete? Result.success("分类删除成功"):Result.error("分类删除失败");
        } catch (Exception e) {
            log.error("删除分类失败: {}", e.getMessage(), e);
            return Result.error("系统错误，,请联系管理员");
        }
    }
}
