package com.swx.ucenter.model.constants;

public class WxAuthURLConstants {
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    public static final String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
}

