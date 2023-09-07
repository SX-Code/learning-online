package com.swx.gateway.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果实体类
 */
@Data
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer code;
    private String message;
    private Object data;
    public ErrorResult() {}

    // 返回失败
    public static ErrorResult fail(Integer code, String message) {
        ErrorResult result = new ErrorResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}

