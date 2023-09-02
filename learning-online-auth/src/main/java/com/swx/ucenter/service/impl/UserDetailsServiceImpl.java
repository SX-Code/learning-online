package com.swx.ucenter.service.impl;

import com.alibaba.fastjson2.JSON;
import com.swx.ucenter.model.dto.AuthParamDTO;
import com.swx.ucenter.model.dto.XcUserExt;
import com.swx.ucenter.model.po.XcMenu;
import com.swx.ucenter.service.AuthService;
import com.swx.ucenter.service.UserDetailsService;
import com.swx.ucenter.service.XcMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 实现自定义UserDetailsService
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   private final ApplicationContext applicationContext;
   private final XcMenuService xcMenuService;

    public UserDetailsServiceImpl(ApplicationContext applicationContext, XcMenuService xcMenuService) {
        this.applicationContext = applicationContext;
        this.xcMenuService = xcMenuService;
    }

    /**
     * 根据账号获取用户对象，获取不到直接抛异常
     *
     * @param account 账号
     * @return 用户完整信息
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        AuthParamDTO authParamDTO = null;
        try {
            authParamDTO = JSON.parseObject(account, AuthParamDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("认证请求参数不符合要求");
        }

        String authType = authParamDTO.getAuthType();
        if (StringUtils.isEmpty(authType)) {
            throw new RuntimeException("未指定认证方式");
        }
        String beanName = authType + "_authService";

        // 根据认证类型从spring容器中取出指定Bean
        AuthService authService = applicationContext.getBean(beanName, AuthService.class);
        // 调用统一execute方法完成认证
        XcUserExt xcUserExt = authService.execute(authParamDTO);

        return getUserPrincipal(xcUserExt);
    }

    /**
     * 查询用户信息
     * @param xcUserExt 用户信息
     * @return UserDetails
     */
    private UserDetails getUserPrincipal(XcUserExt xcUserExt) {
        String password = xcUserExt.getPassword();
        // 根据用户ID查询权限信息
        String[] authorities = {};
        List<String> permissions = xcMenuService.selectPermissionCodeByUserId(xcUserExt.getId());
        if (!CollectionUtils.isEmpty(permissions)) {
            // 转数组
            authorities = permissions.toArray(new String[0]);
        }
        // 转JSON
        xcUserExt.setPassword(null);
        String userJson = JSON.toJSONString(xcUserExt);

        // 封装 UserDetails 对象
        return User.withUsername(userJson)
                .password(password)
                .authorities(authorities).build();
    }
}
