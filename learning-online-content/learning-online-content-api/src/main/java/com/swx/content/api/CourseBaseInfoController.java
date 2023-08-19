package com.swx.content.api;


import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.content.model.dto.AddCourseDTO;
import com.swx.content.model.dto.QueryCourseParamsDTO;
import com.swx.content.model.po.CourseBase;
import com.swx.content.model.vo.CourseBaseInfoVO;
import com.swx.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/course")
    public CourseBaseInfoVO createCourseBase(@RequestBody AddCourseDTO dto) {
        Long companyId = 1232141425L;
        return courseBaseService.createCourseBase(companyId, dto);
    }
}

