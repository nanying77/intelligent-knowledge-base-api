package com.bujian.aipersnonknowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档实体类
 * 对应数据库中的文档表，用于存储各种类型的文档信息
 *
 * @author System
 * @since 2024
 */
@Data
@TableName("document")
public class Document {

    /**
     * 文档ID - 主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文档标题
     */
    @TableField("title")
    private String title;

    /**
     * 文档描述
     */
    @TableField("description")
    private String description;

    /**
     * 文档内容 - 小文档直接存储在此字段中
     */
    @TableField("content")
    private String content;

    /**
     * 内容文件路径 - 大文档使用文件存储时的文件路径
     */
    @TableField("content_path")
    private String contentPath;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件类型
     * markdown: Markdown文档
     * rich_text: 富文本文档
     * pdf: PDF文档
     * word: Word文档
     * image: 图片文件
     * other: 其他类型文件
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件MD5哈希 - 用于文件去重和校验
     */
    @TableField("file_hash")
    private String fileHash;

    /**
     * 编辑器类型
     * markdown: Markdown编辑器
     * rich_text: 富文本编辑器
     */
    @TableField("editor_type")
    private String editorType;

    /**
     * 分类ID - 文档所属分类
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 用户ID - 文档创建者/所有者
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 当前版本号 - 用于版本控制，从1开始
     */
    @TableField("version")
    private Integer version;

    /**
     * 文档状态
     * draft: 草稿
     * published: 已发布
     * archived: 已归档
     */
    @TableField("status")
    private String status;

    /**
     * 是否加密 - true:加密, false:未加密
     */
    @TableField("is_encrypted")
    private Boolean encrypted;

    /**
     * 是否置顶 - true:置顶, false:未置顶
     */
    @TableField("is_pinned")
    private Boolean pinned;

    /**
     * 是否公开 - true:公开, false:私有
     */
    @TableField("is_public")
    private Boolean isPublic;

    /**
     * 浏览次数 - 文档被查看的次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞次数 - 文档被点赞的次数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 是否收藏
     */
    @TableField("is_favorite")
    private Boolean isFavorite;

    /**
     * 评论次数 - 文档被评论的次数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 封面图片 - 文档封面图片路径
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 最后查看时间 - 文档最后一次被查看的时间
     */
    @TableField("last_view_time")
    private LocalDateTime lastViewTime;

    /**
     * 发布时间 - 文档发布的时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 创建时间 - 文档创建时间，插入时自动填充
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间 - 文档最后更新时间，插入和更新时自动填充
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "base_id")
    private String baseId;
}

