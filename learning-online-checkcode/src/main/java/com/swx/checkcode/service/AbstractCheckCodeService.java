package com.swx.checkcode.service;

import com.swx.checkcode.model.CheckCodeParamsDTO;
import com.swx.checkcode.model.CheckCodeResultVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 验证码接口
 */
@Slf4j
public abstract class AbstractCheckCodeService implements CheckCodeService {

    protected CheckCodeGenerator checkCodeGenerator;
    protected KeyGenerator keyGenerator;
    protected CheckCodeStore checkCodeStore;

    public abstract void  setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator);
    public abstract void  setKeyGenerator(KeyGenerator keyGenerator);
    public abstract void  setCheckCodeStore(CheckCodeStore CheckCodeStore);


    /**
     * 生成验证公用方法
     *
     * @param dto        生成验证码参数
     * @param codeLength 验证码长度
     * @param keyPrefix  key的前缀
     * @param expire     过期时间
     * @return com.swx.checkcode.service.AbstractCheckCodeService.GenerateResult 生成结果
     */
    public GenerateResult generate(CheckCodeParamsDTO dto, Integer codeLength, String keyPrefix, Integer expire) {
        // 生成四位验证码
        String code = checkCodeGenerator.generate(codeLength);
        // 生成一个key
        String key = keyGenerator.generate(keyPrefix);

        // 存储验证码
        checkCodeStore.set(key, code, expire);
        // 返回验证码生成结果
        return new GenerateResult(key, code);
    }

    /**
     * 验证方法
     *
     * @param key  key
     * @param code 验证码
     * @return boolean 验证结果
     */
    public boolean verify(String key, String code) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(code)) {
            return false;
        }
        String code_l = checkCodeStore.get(key);
        if (code_l == null) {
            return false;
        }
        boolean result = code_l.equalsIgnoreCase(code);
        if (result) {
            //删除验证码
            checkCodeStore.remove(key);
        }
        return result;
    }


    @Data
    protected static class GenerateResult {
        String key;
        String code;

        GenerateResult() {

        }

        GenerateResult(String key, String code) {
            this.key = key;
            this.code = code;
        }
    }

    public abstract CheckCodeResultVO generate(CheckCodeParamsDTO checkCodeParamsDto);
}
