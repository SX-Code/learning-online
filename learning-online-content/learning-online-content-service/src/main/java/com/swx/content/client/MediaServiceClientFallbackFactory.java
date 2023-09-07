package com.swx.content.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 降级方法，可以拿到异常信息
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {

    @Override
    public MediaServiceClient create(Throwable throwable) {
        return (filedata, objectName) -> {
            log.debug("远程调用媒资上传文件的接口发生熔断: ", throwable);
            return null;
        };
    }
}
