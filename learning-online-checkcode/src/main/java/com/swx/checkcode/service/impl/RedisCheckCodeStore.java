package com.swx.checkcode.service.impl;

import com.swx.checkcode.service.CheckCodeService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis存储验证码
 */
@Component("RedisCheckCodeStore")
public class RedisCheckCodeStore implements CheckCodeService.CheckCodeStore {

    private final StringRedisTemplate redisTemplate;

    public RedisCheckCodeStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向缓存设置key
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间,单位秒
     */
    @Override
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * @param key 键
     * @return 值
     */
    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key 键
     */
    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
