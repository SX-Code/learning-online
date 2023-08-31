package com.swx.content.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {

    @Override
    public SearchServiceClient create(Throwable throwable) {
        return courseIndex -> {
            log.debug("远程调用搜索服务创建课程索引接口发生熔断: ", throwable);
            return false;
        };
    }
}
