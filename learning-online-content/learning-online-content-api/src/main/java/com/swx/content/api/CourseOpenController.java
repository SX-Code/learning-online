package com.swx.content.api;

import com.swx.content.model.vo.CoursePreviewVO;
import com.swx.content.service.CoursePublishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/open")
public class CourseOpenController {

    private final CoursePublishService coursePublishService;

    public CourseOpenController(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
    }

    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewVO preview(@PathVariable("courseId") @NotNull(message = "课程ID不能为空") Long courseId) {
        return coursePublishService.getCoursePreviewInfo(courseId);
    }

}
