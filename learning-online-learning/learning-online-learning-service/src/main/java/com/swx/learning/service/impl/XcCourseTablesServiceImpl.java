package com.swx.learning.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.learning.model.po.XcChooseCourse;
import com.swx.learning.model.po.XcCourseTables;
import com.swx.learning.mapper.XcCourseTablesMapper;
import com.swx.learning.model.vo.XcCourseTablesVO;
import com.swx.learning.service.XcCourseTablesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 *  选课服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-03
 */
@Service
public class XcCourseTablesServiceImpl extends ServiceImpl<XcCourseTablesMapper, XcCourseTables> implements XcCourseTablesService {

    /**
     * 添加到我的课程表
     *
     * @param xcChooseCourse 选课记录
     * @return com.swx.learning.model.po.XcCourseTables 课程表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public XcCourseTables addCourseTables(XcChooseCourse xcChooseCourse) {
        String status = xcChooseCourse.getStatus();
        if (!status.equals("701001")) {
            throw new BizException("选课没有成功，无法添加到课程表");
        }
        XcCourseTables courseTables = getCourseTable(xcChooseCourse.getUserId(), xcChooseCourse.getCourseId());
        if (courseTables != null) {
            return courseTables;
        }
        courseTables = new XcCourseTables();
        BeanUtils.copyProperties(xcChooseCourse, courseTables);
        courseTables.setChooseCourseId(xcChooseCourse.getId()); // 记录选课表的主键
        courseTables.setCourseType(xcChooseCourse.getOrderType());
        courseTables.setUpdateDate(LocalDateTime.now());
        boolean save = save(courseTables);
        if (!save) {
            throw new BizException("添加我的课程表失败");
        }
        return courseTables;
    }



    /**
     * 是否已存在课程表
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return com.swx.learning.model.po.XcChooseCourse 选课表
     */
    @Override
    public XcCourseTables getCourseTable(String userId, Long courseId) {
        return getOne(Wrappers.<XcCourseTables>lambdaQuery()
                .eq(XcCourseTables::getUserId, userId)
                .eq(XcCourseTables::getCourseId, courseId));
    }

    /**
     * 查询学习资格
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return String 学习资格
     */
    @Override
    public XcCourseTablesVO getLearningStatus(String userId, Long courseId) {
        XcCourseTables courseTable = getCourseTable(userId, courseId);
        XcCourseTablesVO xcCourseTablesVO = new XcCourseTablesVO();
        if (courseTable == null) {
            // 选课没支付
            xcCourseTablesVO.setLearnStatus("702002");
            return xcCourseTablesVO;
        }
        boolean before = courseTable.getValidtimeEnd().isBefore(LocalDateTime.now());
        BeanUtils.copyProperties(courseTable, xcCourseTablesVO);
        if (before) {
            // 已过期
            xcCourseTablesVO.setLearnStatus("702003");
        } else {
            // 可以学习
            xcCourseTablesVO.setLearnStatus("702001");
        }
        return xcCourseTablesVO;
    }

}
