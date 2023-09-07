package com.swx.checkcode.model;

import lombok.Data;

/**
 * 验证码参数类
 */
@Data
public class CheckCodeParamsDTO {

    /**
     * 验证码类型:pic、sms、email等
     */
    private String checkCodeType;

    /**
     * 业务携带参数
     */
    private String param1;
    private String param2;
    private String param3;
}
