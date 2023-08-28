package com.swx.content.service.impl;

import com.swx.content.model.po.CoursePublish;
import com.swx.content.mapper.CoursePublishMapper;
import com.swx.content.model.vo.CourseBaseInfoVO;
import com.swx.content.model.vo.CoursePreviewVO;
import com.swx.content.model.vo.TeachPlanVO;
import com.swx.content.service.CourseBaseService;
import com.swx.content.service.CoursePublishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.content.service.TeachPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {

    private final CourseBaseService courseBaseService;
    private final TeachPlanService teachPlanService;

    public CoursePublishServiceImpl(CourseBaseService courseBaseService, TeachPlanService teachPlanService) {
        this.courseBaseService = courseBaseService;
        this.teachPlanService = teachPlanService;
    }

    /**
     * 获取课程预览信息
     *
     * @param courseId 课程ID
     * @return CoursePreviewVO 预览信息
     */
    @Override
    public CoursePreviewVO getCoursePreviewInfo(Long courseId) {
        // 课程基本信息, 营销信息
        CourseBaseInfoVO courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        // 课程计划信息
        List<TeachPlanVO> teachPlans = teachPlanService.getTreeNodes(courseId);
        // 封装VO
        CoursePreviewVO previewVO = new CoursePreviewVO();
        previewVO.setCourseBase(courseBaseInfo);
        previewVO.setTeachplans(teachPlans);
        return previewVO;
    }
}
