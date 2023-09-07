package com.swx.learning.client;

import com.swx.learning.model.dto.CoursePublish;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "content-api", fallbackFactory = ContentServerClientFallbackFactory.class)
public interface ContentServerClient {
    @GetMapping("/content/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId);
}
