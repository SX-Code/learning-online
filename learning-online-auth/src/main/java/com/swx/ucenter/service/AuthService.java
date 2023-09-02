package com.swx.ucenter.service;

import com.swx.ucenter.model.dto.AuthParamDTO;
import com.swx.ucenter.model.dto.XcUserExt;

/**
 * 统一认证接口
 */
public interface AuthService {

    /**
     * 认证方法
     * @param dto 认证参数
     * @return XcUserExt
     */
    public XcUserExt execute(AuthParamDTO dto);
}
