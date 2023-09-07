package com.swx.learning.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "media-api", fallbackFactory = MediaServerClientFallbackFactory.class)
public interface MediaServerClient {
    @GetMapping("/media/files/video/url/{mediaId}")
    public String getPlayUrlByMediaId(@PathVariable("mediaId") String mediaId);
}