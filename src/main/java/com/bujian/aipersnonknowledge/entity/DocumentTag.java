package com.bujian.aipersnonknowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文档标签关联实体类
 * 记录文档和标签的多对多关系
 *
 * @author YourName
 * @since 2024
 */
@Data
@TableName("document_tag")
public class DocumentTag {

    /**
     * 关联ID - 主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文档ID - 关联文档表的ID
     */
    private Integer documentId;

    /**
     * 标签ID - 关联标签表的ID
     */
    private Integer tagId;

    /**
     * 创建时间 - 关联创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}