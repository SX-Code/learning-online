package com.swx.checkcode.service;

import com.swx.checkcode.model.CheckCodeParamsDTO;
import com.swx.checkcode.model.CheckCodeResultVO;

/**
 * 验证码接口
 */
public interface CheckCodeService {

    /**
     * 生成验证码
     *
     * @param dto 生成验证码参数
     * @return com.swx.checkcode.model.CheckCodeResultDTO 验证码结果
     */
    CheckCodeResultVO generate(CheckCodeParamsDTO dto);

    /**
     * 校验验证码
     *
     * @param key  key
     * @param code 验证码
     * @return boolean 验证结果
     */
    public boolean verify(String key, String code);

    /**
     * 验证码生成器
     */
    public interface CheckCodeGenerator {
        /**
         * 验证码生成
         *
         * @param length 验证码长度
         * @return String 验证码
         */
        String generate(int length);
    }

    /**
     * Key生成器
     */
    public interface KeyGenerator {
        /**
         * key生成
         *
         * @param prefix key前缀
         * @return String key
         */
        String generate(String prefix);
    }

    /**
     * 验证码存储
     */
    public interface CheckCodeStore {

        /**
         * 向缓存设置key
         *
         * @param key    key
         * @param value  value
         * @param expire 过期时间,单位秒
         */
        void set(String key, String value, Integer expire);

        String get(String key);

        void remove(String key);
    }
}
