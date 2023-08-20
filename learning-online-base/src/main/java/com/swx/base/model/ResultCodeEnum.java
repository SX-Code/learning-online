package com.swx.base.model;

public enum ResultCodeEnum {

    /* 成功状态码 */
    SUCCESS(200, "成功"),
    /* Token 50~100 */
    TOKEN_INVALID(50, "无效TOKEN"),
    TOKEN_EXPIRE(51, "TOKEN已过期"),
    TOKEN_REQUIRE(50, "TOKEN是必须的"),
    /* 参数错误 500~1000 */
    SERVER_ERROR(500, "服务器内部错误"),
    PARAM_REQUIRE(501, "缺少参数"),
    PARAM_INVALID(502, "无效参数"),
    PARAM_TIMAGE_FORMAT_ERROR(503, "图片格式有误"),
    /* 数据错误 1000~2000 */
    DATA_EXIST(1000, "数据已经存在"),
    DATA_NOT_EXIST(1001, "数据不存在"),
    /* 数据错误 300~3500 */
    NO_OPERATOR_AUTH(3000, "无权操作"),
    NEED_ADMIN(3001, "需要管理员权限");

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }
    public String message() {
        return this.message;
    }
}

