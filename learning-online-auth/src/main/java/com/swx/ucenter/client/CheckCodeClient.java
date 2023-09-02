package com.swx.ucenter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用校验验证码
 */
@FeignClient(value = "checkcode", fallbackFactory = CheckCodeClientFallbackFactory.class)
public interface CheckCodeClient {

    @PostMapping("/checkcode/verify")
    public Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);
}
