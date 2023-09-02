package com.swx.ucenter.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.base.model.ResultCodeEnum;
import com.swx.ucenter.model.constants.WxAuthURLConstants;
import com.swx.ucenter.model.dto.AuthParamDTO;
import com.swx.ucenter.model.dto.XcUserExt;
import com.swx.ucenter.model.po.XcUser;
import com.swx.ucenter.model.po.XcUserRole;
import com.swx.ucenter.service.AuthService;
import com.swx.ucenter.service.WxAuthService;
import com.swx.ucenter.service.XcUserRoleService;
import com.swx.ucenter.service.XcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信扫码认证方式
 */
@Slf4j
@RefreshScope
@Service("wx_authService")
public class WxAuthServiceImpl implements AuthService, WxAuthService {

    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.secret}")
    private String secret;
    private final RestTemplate restTemplate;
    private final XcUserService xcUserService;
    private final XcUserRoleService xcUserRoleService;
    private final TransactionTemplate transactionTemplate;

    public WxAuthServiceImpl(RestTemplate restTemplate, XcUserService xcUserService,
                             XcUserRoleService xcUserRoleService, TransactionTemplate transactionTemplate) {
        this.restTemplate = restTemplate;
        this.xcUserService = xcUserService;
        this.xcUserRoleService = xcUserRoleService;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 认证方法
     *
     * @param dto 认证参数
     * @return XcUserExt
     */
    @Override
    public XcUserExt execute(AuthParamDTO dto) {
        String username = dto.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new BizException(ResultCodeEnum.PARAM_INVALID);
        }
        // 查询数据库
        XcUser xcUser = xcUserService.getOne(Wrappers.<XcUser>lambdaQuery().eq(XcUser::getUsername, username));
        if (xcUser == null) {
            throw new BizException(ResultCodeEnum.DATA_NOT_EXIST);
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        return xcUserExt;
    }

    /**
     * 微信扫码认证，申请令牌, 携带令牌查询用户信息、保存到自己数据库
     *
     * @param code code
     * @return com.swx.ucenter.model.po.XcUser 用户信息
     */
    @Override
    public XcUser wxAuth(String code) {
        // 申请令牌
        Map<String, String> accessTokenMap = getAccessToken(code);
        String accessToken = accessTokenMap.get("access_token");
        String openid = accessTokenMap.get("openid");
        // 携带令牌查询用户信息
        Map<String, String> userInfo = getUserInfo(accessToken, openid);
        // 保存到自己数据库
        return transactionTemplate.execute((status) -> {
            try {
                return addWxUser(userInfo);
            } catch (Exception e) {
                log.error("新增用户失败", e);
                status.setRollbackOnly();
            }
            return null;
        });
    }

    public XcUser addWxUser(Map<String, String> userInfoMap) {
        // unionid，用户在网站上的唯一ID。
        String unionid = userInfoMap.get("unionid");
        String nickname = userInfoMap.get("nickname");
        XcUser xcUser = xcUserService.getOne(Wrappers.<XcUser>lambdaQuery().eq(StringUtils.hasText(unionid), XcUser::getWxUnionid, unionid));
        // 已注册，直接返回
        if (xcUser != null) {
            return xcUser;
        }

        // 新增用户
        xcUser = new XcUser();
        xcUser.setId(UUID.randomUUID().toString());
        xcUser.setWxUnionid(unionid);
        xcUser.setPassword(unionid);
        xcUser.setUsername(unionid);
        xcUser.setNickname(nickname);
        xcUser.setName(nickname);
        xcUser.setCreateTime(LocalDateTime.now());
        xcUser.setUtype("101001");
        xcUser.setStatus("1");
        xcUser.setSex(userInfoMap.get("sex"));
        xcUserService.save(xcUser);

        // 新增用户角色信息
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setUserId(xcUser.getId());
        xcUserRole.setId(UUID.randomUUID().toString());
        xcUserRole.setRoleId("17"); // 17 表示学生角色
        xcUserRole.setCreateTime(LocalDateTime.now());
        xcUserRoleService.save(xcUserRole);

        return xcUser;
    }

    /**
     * 通过code获取access_token
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     *
     * @param code code
     */
    private Map<String, String> getAccessToken(String code) {
        // 填充url
        String url = String.format(WxAuthURLConstants.ACCESS_TOKEN, appid, secret, code);
        // 发起请求
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        String result = exchange.getBody();
        // 解析body
        return handleResult(result);
    }

    /**
     * 获取用户个人信息
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     * {
     * "openid":"OPENID",
     * "nickname":"NICKNAME",
     * "sex":1,
     * "province":"PROVINCE",
     * "city":"CITY",
     * "country":"COUNTRY",
     * "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     * "privilege":[
     * "PRIVILEGE1",
     * "PRIVILEGE2"
     * ],
     * "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     *
     * @param accessToken 令牌
     * @param openId      用户的标识
     */
    private Map<String, String> getUserInfo(String accessToken, String openId) {
        // 填充url
        String url = String.format(WxAuthURLConstants.USER_INFO, accessToken, openId);
        // 发起请求
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String result = exchange.getBody();
        // 解析body
        return handleResult(result);
    }

    /**
     * 解析body
     *
     * @param body body
     */
    private Map<String, String> handleResult(String body) {
        if (StringUtils.isEmpty(body)) {
            log.error("请求access_token出错, body为空");
            throw new BizException("请求access_token出错");
        }
        String body_utf8 = new String(body.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 将result转为Map
        Map<String, String> map = JSON.parseObject(body_utf8, new TypeReference<HashMap<String, String>>() {});
        // 错误判断
        if (StringUtils.hasText(map.get("errcode"))) {
            String errmsg = map.get("errmsg");
            String errcode = map.get("errcode");
            log.error("请求access_token出错, errcode: {}, errmsg: {}", errcode, errmsg);
            throw new BizException(errmsg);
        }
        return map;
    }
}
