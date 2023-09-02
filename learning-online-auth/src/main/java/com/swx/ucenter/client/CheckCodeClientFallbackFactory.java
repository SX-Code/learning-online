package com.swx.ucenter.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckCodeClientFallbackFactory implements FallbackFactory<CheckCodeClient> {
    /**
     * @param throwable
     * @return
     */
    @Override
    public CheckCodeClient create(Throwable throwable) {
       return (key, code) -> {
           log.error("远程调用校验验证码服务失败，熔断降级, key: {}, code: {}", key, code);
           return false;
       };
    }
}
