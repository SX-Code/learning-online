package com.swx.content.api;

import com.swx.base.exception.ResponseResult;
import com.swx.base.model.R;
import com.swx.content.model.po.CoursePublish;
import com.swx.content.service.CoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "课程发布审核接口", tags = "课程发布审核接口")
@ResponseResult
@RestController
public class CoursePublishController {

    private final CoursePublishService coursePublishService;

    public CoursePublishController(CoursePublishService coursePublishService) {
        this.coursePublishService = coursePublishService;
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

    /**
     * 其他微服务远程调用，不用授权
     *
     * @param courseId 课程ID
     */
    @ApiOperation("查询课程发布信息")
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
        return coursePublishService.getById(courseId);
    }

}
