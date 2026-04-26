package com.bujian.aipersnonknowledge.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVo {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private int id;

    private String username;

    private String password;

    private String newPassword;

    private String email;

    private String nickname;

    private String avatar;

    private LocalDateTime createTime;

    private Integer role;
}
