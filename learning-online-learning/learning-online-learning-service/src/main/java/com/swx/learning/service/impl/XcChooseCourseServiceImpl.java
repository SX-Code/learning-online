package com.swx.learning.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.learning.model.dto.CoursePublish;
import com.swx.learning.model.po.XcChooseCourse;
import com.swx.learning.mapper.XcChooseCourseMapper;
import com.swx.learning.service.XcChooseCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-03
 */
@Service
public class XcChooseCourseServiceImpl extends ServiceImpl<XcChooseCourseMapper, XcChooseCourse> implements XcChooseCourseService {


    /**
     * 添加免费课程
     *
     * @param userId        用户ID
     * @param coursePublish 课程信息
     * @return com.swx.learning.model.po.XcChooseCourse 选课记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public XcChooseCourse addFreeCourse(String userId, CoursePublish coursePublish) {
        return addCourse(userId, coursePublish, false);
    }

    /**
     * 添加收费课程
     *
     * @param userId        用户ID
     * @param coursePublish 课程信息
     * @return com.swx.learning.model.po.XcChooseCourse 选课记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public XcChooseCourse addChargeCourse(String userId, CoursePublish coursePublish) {
        return addCourse(userId, coursePublish, true);
    }


    /**
     * 添加课程
     *
     * @param userId        用户ID
     * @param coursePublish 课程信息
     * @param isCharge      是否收费
     * @return com.swx.learning.model.po.XcChooseCourse 选课记录
     */
    public XcChooseCourse addCourse(String userId, CoursePublish coursePublish, boolean isCharge) {
        String status = "701001"; // 选课成功
        String orderType = "700001"; // 免费课程
        if (isCharge) {
            status = "701002"; // 待支付
            orderType = "700002"; // 收费课程
        }
        Long courseId = coursePublish.getId();
        // 查询是否已选
        List<XcChooseCourse> chooseCourses = list(Wrappers.<XcChooseCourse>lambdaQuery()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, courseId)
                .eq(XcChooseCourse::getStatus, status) // 待支付
                .eq(XcChooseCourse::getOrderType, orderType));// 收费课程
        if (!chooseCourses.isEmpty()) {
            return chooseCourses.get(0);
        }

        // 向选课记录表写入数据
        XcChooseCourse chooseCourse = new XcChooseCourse();
        chooseCourse.setCourseId(courseId);
        chooseCourse.setCourseName(coursePublish.getName());
        chooseCourse.setCompanyId(coursePublish.getCompanyId());
        chooseCourse.setUserId(userId);
        chooseCourse.setOrderType(orderType);
        chooseCourse.setCreateDate(LocalDateTime.now());
        chooseCourse.setCoursePrice(coursePublish.getPrice());
        chooseCourse.setValidDays(365);
        chooseCourse.setStatus(status);
        chooseCourse.setValidtimeStart(LocalDateTime.now());
        chooseCourse.setValidtimeEnd(chooseCourse.getValidtimeStart().plusDays(chooseCourse.getValidDays()));
        boolean save = save(chooseCourse);
        if (!save) {
            throw new BizException("添加选课记录失败");
        }
        return chooseCourse;
    }
}
