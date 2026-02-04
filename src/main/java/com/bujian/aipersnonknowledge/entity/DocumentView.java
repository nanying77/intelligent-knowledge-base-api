package com.bujian.aipersnonknowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author MuQin
 */
@Data
@TableName("document_view")
public class DocumentView {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    // 浏览用户ID

    private Integer documentId;
    // 浏览文档ID

    private LocalDateTime viewTime;
    // 浏览时间

    private Integer viewDuration;
    // 浏览时长（秒）
}