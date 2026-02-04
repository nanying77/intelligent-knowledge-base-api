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
@TableName("document_version")
public class DocumentVersion {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer documentId;
    // 关联文档ID

    private String content;
    // 历史版本内容

    private Integer versionNum;
    // 版本号（从1递增）

    private Integer updateUserId;
    // 更新人ID（关联user表）

    private String updateDesc;
    // 版本更新说明

    private LocalDateTime createTime;
    // 版本创建时间
}