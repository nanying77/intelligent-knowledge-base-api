package com.bujian.aipersnonknowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类实体类
 */
@Data
@TableName("category")
public class Category {

    /**
     * 分类ID - 主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer  id;

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
    private Integer parentId ;

    /**
     * 用户ID - 分类所属用户
     */
    private Integer userId;

    /**
     * 排序顺序 - 用于分类排序
     */
    private Integer sortOrder  ;

    /**
     * 分类图标 - 图标名称或路径
     */
    private String icon;

    /**
     * 文档数量 - 该分类下的文档总数
     */
    private Integer documentCount ;

    /**
     * 是否系统分类 - true:系统分类, false:用户分类
     */
    private Boolean isSystem = false;

    /**
     * 创建时间 - 插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间 - 插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}