package com.swx.learning.utils;

import com.alibaba.fastjson2.JSON;
import com.swx.base.exception.BizException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 获取当前用户身份工具类
 */
@Slf4j
public class SecurityUtil {

    public static XcUser getUser() {
        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof String) {
                // 取出用户身份信息
                String principal = principalObj.toString();
                return JSON.parseObject(principal, XcUser.class);
            }
            throw new RuntimeException();
        } catch (Exception e) {
            log.error("获取当前登陆用户身份出错", e);
            throw new BizException("获取当前登陆用户身份出错");
        }
    }

    @Data
    public static class XcUser implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String username;
        private String salt;
        private String wxUnionid;
        private String nickname;
        private String name;
        private String userpic;
        private String companyId;
        private String utype;
        private LocalDateTime birthday;
        private String sex;
        private String email;
        private String cellphone;
        private String qq;
        private String status;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }
}
