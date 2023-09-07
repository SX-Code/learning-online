package com.swx.learning.controller;

import com.swx.base.model.PageResult;
import com.swx.learning.model.dto.MyCourseTableParamsDTO;
import com.swx.learning.model.po.XcCourseTables;
import com.swx.learning.model.vo.XcChooseCourseVO;
import com.swx.learning.model.vo.XcCourseTablesVO;
import com.swx.learning.service.MyCourseTablesService;
import com.swx.learning.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "我的课程表接口", tags = "我的课程表接口")
@RestController
public class MyCourseTablesController {

    private final MyCourseTablesService myCourseTablesService;

    public MyCourseTablesController(MyCourseTablesService myCourseTablesService) {
        this.myCourseTablesService = myCourseTablesService;
    }

    @ApiOperation("添加选课")
    @PostMapping("/choosecourse/{courseId}")
    public XcChooseCourseVO addChooseCourse(@PathVariable("courseId") Long courseId) {
        // 获取userId
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        String userId = user.getId();
        return myCourseTablesService.addChooseCourse(userId, courseId);
    }

    @ApiOperation("查询学习资格")
    @PostMapping("/choosecourse/learnstatus/{courseId}")
    public XcCourseTablesVO getLearningStatus(@PathVariable("courseId") Long courseId) {
        // 获取userId
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        String userId = user.getId();
        return myCourseTablesService.getLearningStatus(userId, courseId);
    }

    @ApiOperation("我的课程表")
    @GetMapping("/mycoursetable")
    public PageResult<XcCourseTables> getMyCourseTables(MyCourseTableParamsDTO dto) {
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        dto.setUserId(user.getId());
        return myCourseTablesService.getMyCourseTables(dto);
    }
}
