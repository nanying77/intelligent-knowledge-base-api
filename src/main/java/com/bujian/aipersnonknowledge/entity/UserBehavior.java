package com.bujian.aipersnonknowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户行为实体类
 * 用于记录用户的各种操作行为，支持行为分析和审计
 */
@Data
@TableName("user_behavior")
public class UserBehavior implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 行为ID - 主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;
    
    /**
     * 行为类型
     * login: 登录
     * logout: 登出
     * create: 创建
     * update: 更新
     * delete: 删除
     * view: 查看
     * download: 下载
     * share: 分享
     * favorite: 收藏
     * like: 点赞
     * comment: 评论
     */
    @TableField("action_type")
    private String actionType;
    
    /**
     * 目标类型
     * document: 文档
     * knowledge: 知识库
     * category: 分类
     * user: 用户
     * system: 系统
     */
    @TableField("target_type")
    private String targetType;
    
    /**
     * 目标ID
     */
    @TableField("target_id")
    private Integer targetId;
    
    /**
     * 行为详情（JSON格式）
     * 例如: {"title": "文档标题", "baseId": "123"}
     */
    @TableField("action_detail")
    private String actionDetail;
    
    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;
    
    /**
     * 用户代理（浏览器信息）
     */
    @TableField("user_agent")
    private String userAgent;
    
    /**
     * 创建时间 - 行为发生时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
