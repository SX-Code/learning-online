package com.swx.auth.controller;

import com.swx.ucenter.model.po.XcUser;
import com.swx.ucenter.service.XcUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一登陆认证接口
 */
@Api(value = "统一登陆认证接口", tags = "统一登陆认证接口")
@RestController
public class LoginController {

    private final XcUserService xcUserService;

    public LoginController(XcUserService xcUserService) {
        this.xcUserService = xcUserService;
    }

    @RequestMapping("/login-success")
    public String loginSuccess() {

        return "登陆成功";
    }

    @RequestMapping("/user/{id}")
    public XcUser getUser(@PathVariable("id") String id) {
        return xcUserService.getById(id);
    }

    @RequestMapping("/r/r1")
    public String r1() {
        return "访问r1资源";
    }

    @RequestMapping("/r/r2")
    public String r2() {
        return "访问r2资源";
    }
}
