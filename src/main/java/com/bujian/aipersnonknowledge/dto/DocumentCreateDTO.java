package com.bujian.aipersnonknowledge.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentCreateDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "知识库ID不能为空")
    private Long knowledgeBaseId;

    @NotBlank(message = "文档标题不能为空")
    private String title;
    private String content;
    private String description;
}