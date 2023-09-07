package com.swx.ucenter.service;

import com.swx.ucenter.model.po.XcUser;

/**
 * 微信扫码接口
 */
public interface WxAuthService {

    /**
     * 微信扫码认证，携带令牌查询用户信息、保存到自己数据库
     *
     * @param code code
     * @return com.swx.ucenter.model.po.XcUser 用户信息
     */
    public XcUser wxAuth(String code);

}
