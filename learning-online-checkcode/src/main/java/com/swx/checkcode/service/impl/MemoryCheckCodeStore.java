package com.swx.checkcode.service.impl;

import com.swx.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("MemoryCheckCodeStore")
public class MemoryCheckCodeStore implements CheckCodeService.CheckCodeStore {

    Map<String, String> map = new HashMap<String, String>();

    /**
     * 向缓存设置key
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间,单位秒
     */
    @Override
    public void set(String key, String value, Integer expire) {
        map.put(key, value);
    }

    /**
     * @param key 值
     * @return 键
     */
    @Override
    public String get(String key) {
        return map.get(key);
    }

    /**
     * @param key 值
     */
    @Override
    public void remove(String key) {
        map.remove(key);
    }
}
