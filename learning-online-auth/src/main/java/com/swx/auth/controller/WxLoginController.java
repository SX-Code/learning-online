package com.swx.auth.controller;

import com.swx.base.exception.BizException;
import com.swx.ucenter.model.po.XcUser;
import com.swx.ucenter.service.WxAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class WxLoginController {

    private final WxAuthService wxAuthService;

    public WxLoginController(WxAuthService wxAuthService) {
        this.wxAuthService = wxAuthService;
    }

    @RequestMapping("/wxLogin")
    public String wxLogin(String code, String state) {
        log.debug("微信扫码回调, code: {}, state: {}", code, state);
        if (StringUtils.isEmpty(code)) {
            throw new BizException("无code参数");
        }
        XcUser xcUser = wxAuthService.wxAuth(code);
        String username = xcUser.getUsername();
        return "redirect:http://www.51xuecheng.cn/sign.html?username=" + username + "&authType=wx";
    }

}