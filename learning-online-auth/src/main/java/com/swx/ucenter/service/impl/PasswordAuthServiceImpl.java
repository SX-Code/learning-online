package com.swx.ucenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.ucenter.client.CheckCodeClient;
import com.swx.ucenter.model.dto.AuthParamDTO;
import com.swx.ucenter.model.dto.XcUserExt;
import com.swx.ucenter.model.po.XcUser;
import com.swx.ucenter.service.AuthService;
import com.swx.ucenter.service.XcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 密码认证方式实现类
 */
@Slf4j
@Service("password_authService")
public class PasswordAuthServiceImpl implements AuthService {
    private final XcUserService xcUserService;
    private final PasswordEncoder passwordEncoder;
    private final CheckCodeClient checkCodeClient;

    public PasswordAuthServiceImpl(XcUserService xcUserService, PasswordEncoder passwordEncoder, CheckCodeClient checkCodeClient) {
        this.xcUserService = xcUserService;
        this.passwordEncoder = passwordEncoder;
        this.checkCodeClient = checkCodeClient;
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

        // 校验验证码
        String key = dto.getCheckcodekey();
        String code = dto.getCheckcode();
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(code)) {
            log.debug("认证失败: 验证码参数不完整");
            throw new BadCredentialsException("验证码参数不完整");
        }

        // 远程调用校验验证码服务，检验
        Boolean verify = checkCodeClient.verify(key, code);
        if (!verify) {
            log.debug("认证失败: 验证码不合法");
            throw new BadCredentialsException("验证码不合法");
        }
        // 查询数据库
        XcUser dbXcUser = Optional.ofNullable(xcUserService.getOne(Wrappers.<XcUser>lambdaQuery().eq(XcUser::getUsername, username)))
                .orElseThrow(() -> new UsernameNotFoundException("账号不存在"));
        //dbXcUser.getStatus()

        // 检验密码
        String password = dbXcUser.getPassword();
        if (!passwordEncoder.matches(dto.getPassword(), password)) {
            log.debug("认证失败: 密码错误");
            throw new BadCredentialsException("密码错误");
        }

        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(dbXcUser, xcUserExt);
        return xcUserExt;
    }
}
