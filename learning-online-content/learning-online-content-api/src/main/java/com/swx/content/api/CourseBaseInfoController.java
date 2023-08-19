package com.swx.content.api;


import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.content.model.dto.QueryCourseParamsDTO;
import com.swx.content.model.po.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

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

    @ApiOperation("课程查询接口")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParam pageParam, @RequestBody(required = false) QueryCourseParamsDTO dto) {
        PageResult<CourseBase> courseBasePageResult = new PageResult<>();
        ArrayList<CourseBase> courseBases = new ArrayList<>();
        CourseBase courseBase = new CourseBase();
        courseBase.setCreateDate(LocalDateTime.now());
        courseBases.add(courseBase);
        courseBasePageResult.setItems(courseBases);
        return courseBasePageResult;
    }

}

