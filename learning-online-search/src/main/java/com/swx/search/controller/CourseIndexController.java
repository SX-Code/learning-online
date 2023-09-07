package com.swx.search.controller;

import com.swx.base.exception.BizException;
import com.swx.search.po.CourseIndex;
import com.swx.search.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "课程信息索引接口", tags = "课程信息索引接口")
@RestController
@RequestMapping("/index")
@RefreshScope
public class CourseIndexController {

    @Value("${elasticsearch.course.index}")
    private String courseIndexStore;
    private final IndexService indexService;

    public CourseIndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @ApiOperation("添加课程索引")
    @PostMapping("course")
    public Boolean add(@RequestBody @Validated CourseIndex courseIndex) {
        Long id = courseIndex.getId();
        Boolean result = indexService.addCourseIndex(courseIndexStore, String.valueOf(id), courseIndex);
        if (!result) {
            throw new BizException("添加课程索引失败");
        }
        return true;
    }
}
