package com.swx.content.client;

import com.swx.content.model.po.CoursePublish;
import com.swx.content.service.CoursePublishService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoursePublishClient {
    private final CoursePublishService coursePublishService;

    public CoursePublishClient(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
    }

    @ApiOperation("查询课程发布信息")
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
        return coursePublishService.getById(courseId);
    }
}
