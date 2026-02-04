package com.bujian.aipersnonknowledge.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类实体类
 */
@Data
@TableName("category")
public class CategoryVo {

    /**
     * 分类ID - 主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private int id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父分类ID - 0表示根分类
     */
    private int parentId ;

    /**
     * 排序顺序 - 用于分类排序
     */
    private int sortOrder  ;

    /**
     * 文档数量 - 该分类下的文档总数
     */
    private int documentCount ;

    /**
     * 是否系统分类 - true:系统分类, false:用户分类
     */
    private Boolean isSystem ;

    /**
     * 用户ID - 分类所属用户
     */
    private int userId;

    /**
     * 图标
     */
    private String  icon;

}