package com.bujian.aipersnonknowledge.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Result<T> implements Serializable {
    // 常量定义
    public static final Integer SUCCESS_CODE = 200;
    public static final Integer ERROR_CODE = 500;
    public static final Integer UNAUTHORIZED_CODE = 401;
    public static final Integer FORBIDDEN_CODE = 403;
    public static final Integer PARAM_ERROR_CODE = 400;

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;
    private String formattedTimestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
        this.formattedTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage("success");
        // 不要设置 data，让它为 null
        return result;
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    // 成功响应（带消息和数据）
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 成功响应（仅带消息）
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setCode(SUCCESS_CODE);
        result.setMessage(message);
        // data 为 null
        return result;
    }

    // 通用错误响应
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        // data 为 null
        return result;
    }

    // 默认500错误
    public static <T> Result<T> error(String message) {
        return error(ERROR_CODE, message);
    }

    // 认证错误（401）
    public static <T> Result<T> unauthorized(String message) {
        return error(UNAUTHORIZED_CODE, message);
    }

    // 权限错误（403）
    public static <T> Result<T> forbidden(String message) {
        return error(FORBIDDEN_CODE, message);
    }

    // 参数错误（400）
    public static <T> Result<T> paramError(String message) {
        return error(PARAM_ERROR_CODE, message);
    }
}