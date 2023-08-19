package com.swx.content.api;


import com.swx.content.model.vo.CourseCategoryTreeVO;
import com.swx.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 课程分类信息 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Api(value = "课程分类信息管理接口")
@RestController
@RequestMapping("/course-category")
public class CourseCategoryController {

    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

    @ApiOperation("课程查询接口")
    @GetMapping("/tree-nodes")
    public List<CourseCategoryTreeVO> treeNodes() {
        return courseCategoryService.treeNodes("1");
    }

}

