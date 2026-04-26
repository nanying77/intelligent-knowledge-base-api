package com.bujian.aipersnonknowledge.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 短信验证码表实体类
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("base_sms_code")
public class SmsCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 创建人
     */
    @TableField("`create_by`")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`create_time`")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("`update_by`")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`update_time`")
    private Date updateTime;

    /**
     * 手机号码
     */
    @TableField("`phone`")
    private String phone;

    /**
     * 验证码
     */
    @TableField("`sms_code`")
    private String smsCode;

    /**
     * 发送ip
     */
    @TableField("`ip`")
    private String ip;

    /**
     * 过期时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`expiration_time`")
    private Date expirationTime;

}
