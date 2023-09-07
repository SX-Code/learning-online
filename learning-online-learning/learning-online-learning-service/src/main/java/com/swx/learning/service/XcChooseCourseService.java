package com.swx.learning.service;

import com.swx.learning.model.dto.CoursePublish;
import com.swx.learning.model.po.XcChooseCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-03
 */
public interface XcChooseCourseService extends IService<XcChooseCourse> {
    /**
     * 添加免费课程
     *
     * @param userId        用户ID
     * @param coursePublish 课程信息
     * @return com.swx.learning.model.po.XcChooseCourse 选课记录
     */
    XcChooseCourse addFreeCourse(String userId, CoursePublish coursePublish);

    /**
     * 添加收费课程
     *
     * @param userId        用户ID
     * @param coursePublish 课程信息
     * @return com.swx.learning.model.po.XcChooseCourse 选课记录
     */
    XcChooseCourse addChargeCourse(String userId, CoursePublish coursePublish);
}
