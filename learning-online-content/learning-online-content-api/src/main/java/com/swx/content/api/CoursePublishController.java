package com.swx.content.api;

import com.swx.content.model.vo.CoursePreviewVO;
import com.swx.content.service.CoursePublishService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;

@Controller
public class CoursePublishController {

    private final CoursePublishService coursePublishService;

    public CoursePublishController(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
    }

    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") @NotNull(message = "课程ID不能为空") Long courseId) {
        ModelAndView data = new ModelAndView();
        CoursePreviewVO previewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        data.addObject("model", previewInfo);
        data.setViewName("course_template");
        return data;
    }
}
