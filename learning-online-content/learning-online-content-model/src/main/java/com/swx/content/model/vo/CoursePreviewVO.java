package com.swx.content.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 课程预览
 */
@Data
public class CoursePreviewVO {

    // 课程基本信息, 营销信息
    private CourseBaseInfoVO courseBase;

    // 课程计划信息
    private List<TeachPlanVO> teachplans;
}
