package com.swx.search.controller;

import com.swx.base.model.PageParam;
import com.swx.search.dto.SearchCourseParamDTO;
import com.swx.search.vo.SearchPageResultVO;
import com.swx.search.po.CourseIndex;
import com.swx.search.service.CourseSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 课程搜索接口
 */
@Api(value = "课程搜索接口", tags = "课程搜索接口")
@RestController
@RequestMapping("/course")
public class CourseSearchController {

    private final CourseSearchService courseSearchService;

    public CourseSearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }


    @ApiOperation("课程搜索列表")
    @GetMapping("/list")
    public SearchPageResultVO<CourseIndex> list(PageParam pageParam, SearchCourseParamDTO searchCourseParamDTO) {
        return courseSearchService.queryCoursePubIndex(pageParam, searchCourseParamDTO);
    }

}
