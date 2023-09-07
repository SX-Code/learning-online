package com.swx.content.api;

import com.swx.base.exception.ResponseResult;
import com.swx.base.model.R;
import com.swx.content.model.po.CoursePublish;
import com.swx.content.model.vo.CoursePreviewVO;
import com.swx.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Api(value = "课程发布审核接口", tags = "课程发布审核接口")
@ResponseResult
@RestController
public class CoursePublishController {

    private final CoursePublishService coursePublishService;

    public CoursePublishController(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
    }

    @ApiOperation("获取课程发布信息")
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewVO getCOursePublish(@PathVariable("courseId") @NotNull(message = "课程ID不能为空") Long courseId) {
        return coursePublishService.getCoursePublishCache(courseId);
        // return coursePublishService.getCoursePublish(courseId);
    }

    @ApiOperation("提交审核")
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId, courseId);
    }

    @ApiOperation("课程发布")
    @PostMapping("/coursepublish/{courseId}")
    public void coursePublish(@PathVariable("courseId") Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.publish(companyId, courseId);
    }

}
