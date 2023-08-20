package com.swx.content.api;


import com.swx.base.exception.ResponseResult;
import com.swx.base.exception.ValidationGroup;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.content.model.dto.AddCourseDTO;
import com.swx.content.model.dto.EditCourseDTO;
import com.swx.content.model.dto.QueryCourseParamsDTO;
import com.swx.content.model.po.CourseBase;
import com.swx.content.model.vo.CourseBaseInfoVO;
import com.swx.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Api(value = "课程基本信息管理接口")
@RestController
@ResponseResult
@RequestMapping("/course")
public class CourseBaseInfoController {

    private final CourseBaseService courseBaseService;

    public CourseBaseInfoController(CourseBaseService courseBaseService) {
        this.courseBaseService = courseBaseService;
    }

    @ApiOperation("课程查询接口")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParam pageParam, @RequestBody(required = false) QueryCourseParamsDTO dto) {
        return courseBaseService.queryCourseBaseList(pageParam, dto);
    }

    @ApiOperation("新增课程")
    @PostMapping("")
    public CourseBaseInfoVO createCourseBase(@RequestBody @Validated(ValidationGroup.Insert.class) AddCourseDTO dto) {
        // TODO 暂时使用固定值
        Long companyId = 1232141425L;
        return courseBaseService.createCourseBase(companyId, dto);
    }

    @ApiOperation("根据课程id查询接口")
    @GetMapping("/{courseId}")
    public CourseBaseInfoVO list(@PathVariable("courseId") Long courseId) {
        return courseBaseService.getCourseBaseInfo(courseId);
    }

    @ApiOperation("修改课程")
    @PutMapping("")
    public CourseBaseInfoVO updateCourseBase(@RequestBody @Validated EditCourseDTO dto) {
        // TODO 暂时使用固定值
        Long companyId = 1232141425L;
        return courseBaseService.updateCourseBase(companyId, dto);
    }
}

