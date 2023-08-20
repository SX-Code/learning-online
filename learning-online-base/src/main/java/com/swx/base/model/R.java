package com.swx.base.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果实体类
 */
@Data
public class R implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public R() {

    }

    public R(ResultCodeEnum resultCode, Object data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    private void setResultCode(ResultCodeEnum resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    // 返回成功
    public static R success() {
        R result = new R();
        result.setResultCode(ResultCodeEnum.SUCCESS);
        return result;
    }
    // 返回成功
    public static R success(Object data) {
        R result = new R();
        result.setResultCode(ResultCodeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    // 返回失败
    public static R fail(Integer code, String message) {
        R result = new R();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    // 返回失败
    public static R fail(ResultCodeEnum resultCode) {
        R result = new R();
        result.setResultCode(resultCode);
        return result;
    }
}

