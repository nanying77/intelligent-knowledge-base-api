package com.bujian.aipersnonknowledge.vo;

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
public class DocumentVo{

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
     * 文件名
     */
    @TableField("file_name")
    private String fileName;



    /**
     * 文件MD5哈希 - 用于文件去重和校验
     */
    @TableField("file_hash")
    private String fileHash;


    /**
     * 用户ID - 文档创建者/所有者
     */
    @TableField("user_id")
    private Long userId;


    /**
     * 文档状态
     * draft: 草稿
     * published: 已发布
     * archived: 已归档
     */
    @TableField("status")
    private String status;



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
     * 收藏次数 - 文档被收藏的次数
     */
    @TableField("favorite_count")
    private Integer favoriteCount;

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

    /**
     * 知识库Id
     */
    private Integer  baseId;
}