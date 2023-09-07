package com.swx.learning.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MediaServerClientFallbackFactory implements FallbackFactory<MediaServerClient> {

    @Override
    public MediaServerClient create(Throwable throwable) {
        return mediaId -> {
            log.debug("远程调用媒资查询媒资URL失败，走降级方法, mediaId: {}", mediaId);
            return null;
        };
    }
}
