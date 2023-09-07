package com.swx.ucenter.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    /**
     * 根据账号获取用户对象，获取不到直接抛异常
     *
     * @param account 账号
     * @return 用户完整信息
     */
    @Override
    UserDetails loadUserByUsername(String account) throws UsernameNotFoundException;
}
