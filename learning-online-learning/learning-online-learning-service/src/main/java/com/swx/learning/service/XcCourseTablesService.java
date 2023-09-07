package com.swx.learning.service;

import com.swx.learning.model.po.XcChooseCourse;
import com.swx.learning.model.po.XcCourseTables;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.learning.model.vo.XcCourseTablesVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-03
 */
public interface XcCourseTablesService extends IService<XcCourseTables> {

    /**
     * 添加到我的课程表
     *
     * @param xcChooseCourse 选课记录
     * @return com.swx.learning.model.po.XcCourseTables 课程表
     */
    XcCourseTables addCourseTables(XcChooseCourse xcChooseCourse);

    /**
     * 是否已存在课程表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return com.swx.learning.model.po.XcChooseCourse 选课表
     */
    XcCourseTables getCourseTable(String userId, Long courseId);

    /**
     * 查询学习资格
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return String 学习资格
     */
    XcCourseTablesVO getLearningStatus(String userId, Long courseId);
}
