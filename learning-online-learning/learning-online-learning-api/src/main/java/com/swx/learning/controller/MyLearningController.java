package com.swx.learning.controller;

import com.swx.base.model.R;
import com.swx.learning.service.LearningService;
import com.swx.learning.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "学习过程管理接口", tags = "学习过程管理接口")
@RestController
public class MyLearningController {
    private final LearningService learningService;

    public MyLearningController(LearningService learningService) {
        this.learningService = learningService;
    }

    @ApiOperation("获取视频")
    @GetMapping("/open/learn/getvideo/{courseId}/{teachplanId}/{mediaId}")
    public R getVideo(@PathVariable("courseId") Long courseId, @PathVariable("teachplanId") Long teachplanId, @PathVariable("mediaId") String mediaId) {
        String userId = null;
        try {
            SecurityUtil.XcUser user = SecurityUtil.getUser();
            userId = user.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return learningService.getvideo(userId, courseId, teachplanId, mediaId);
    }
}
