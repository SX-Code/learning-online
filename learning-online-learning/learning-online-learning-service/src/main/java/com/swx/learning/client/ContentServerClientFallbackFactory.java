package com.swx.learning.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContentServerClientFallbackFactory implements FallbackFactory<ContentServerClient> {

    @Override
    public ContentServerClient create(Throwable throwable) {
        return courseId -> {
            log.error("远程调用内容服务熔断降级: 课程ID: {}", courseId);
            return null;
        };
    }
}
