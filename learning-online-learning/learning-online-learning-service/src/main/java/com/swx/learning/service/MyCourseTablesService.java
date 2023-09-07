package com.swx.learning.service;

import com.swx.base.model.PageResult;
import com.swx.learning.model.dto.MyCourseTableParamsDTO;
import com.swx.learning.model.po.XcCourseTables;
import com.swx.learning.model.vo.XcChooseCourseVO;
import com.swx.learning.model.vo.XcCourseTablesVO;

public interface MyCourseTablesService {
    /**
     * 添加选课
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return com.swx.learning.model.vo.XcChooseCourseVO 选课信息
     */
    public XcChooseCourseVO addChooseCourse(String userId, Long courseId);

    /**
     * 查询学习资格
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return com.swx.learning.model.vo.XcChooseCourseVO 选课信息
     */
    public XcCourseTablesVO getLearningStatus(String userId, Long courseId);

    /**
     * 支付选课记录成功，更新选课记录表，新增课程信息
     * @param chooseCourseId 选课ID
     * @return 是否更新成功
     */
    public boolean payChooseCourseSuccess(String chooseCourseId);

    /**
     * 查询我的课程表
     * @param dto 查询参数
     */
    PageResult<XcCourseTables> getMyCourseTables(MyCourseTableParamsDTO dto);
}
