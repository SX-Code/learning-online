package com.swx.learning.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.base.exception.BizException;
import com.swx.base.model.PageResult;
import com.swx.learning.client.ContentServerClient;
import com.swx.learning.model.dto.CoursePublish;
import com.swx.learning.model.dto.MyCourseTableParamsDTO;
import com.swx.learning.model.po.XcChooseCourse;
import com.swx.learning.model.po.XcCourseTables;
import com.swx.learning.model.vo.XcChooseCourseVO;
import com.swx.learning.model.vo.XcCourseTablesVO;
import com.swx.learning.service.MyCourseTablesService;
import com.swx.learning.service.XcChooseCourseService;
import com.swx.learning.service.XcCourseTablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 选课相关服务
 */
@Slf4j
@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService {
    private final XcChooseCourseService xcChooseCourseService;
    private final XcCourseTablesService xcCourseTablesService;
    private final ContentServerClient contentServerClient;

    public MyCourseTablesServiceImpl(XcChooseCourseService xcChooseCourseService, XcCourseTablesService xcCourseTablesService, ContentServerClient contentServerClient) {
        this.xcChooseCourseService = xcChooseCourseService;
        this.xcCourseTablesService = xcCourseTablesService;
        this.contentServerClient = contentServerClient;
    }

    /**
     * 添加选课
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return com.swx.learning.model.vo.XcChooseCourseVO 选课信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public XcChooseCourseVO addChooseCourse(String userId, Long courseId) {
        // 远程调用内容管理查询课程的收费规则
        CoursePublish coursePublish = Optional.ofNullable(contentServerClient.getCoursePublish(courseId))
                .orElseThrow(() -> new BizException("课程不存在"));

        // 收费规则
        String charge = coursePublish.getCharge();
        // 选课记录
        XcChooseCourse chooseCourse = null;
        if ("201000".equals(charge)) {
            // 免费课程，会向选课记录表和我的课程表写数据
            chooseCourse = xcChooseCourseService.addFreeCourse(userId, coursePublish);
            XcCourseTables xcCourseTables = xcCourseTablesService.addCourseTables(chooseCourse);
        } else {
            // 收费课程，选课记录表写入数据
            chooseCourse = xcChooseCourseService.addChargeCourse(userId, coursePublish);
        }

        XcChooseCourseVO xcChooseCourseVO = new XcChooseCourseVO();
        BeanUtils.copyProperties(chooseCourse, xcChooseCourseVO);
        XcCourseTablesVO xcCourseTablesVO = getLearningStatus(userId, courseId);
        xcChooseCourseVO.setLearnStatus(xcCourseTablesVO.getLearnStatus());
        return xcChooseCourseVO;
    }

    /**
     * 查询学习资格
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return com.swx.learning.model.vo.XcChooseCourseVO 选课信息
     */
    @Override
    public XcCourseTablesVO getLearningStatus(String userId, Long courseId) {
        // 查询我的课程表，有记录可学习
        return xcCourseTablesService.getLearningStatus(userId, courseId);
    }

    /**
     * 支付选课记录成功，更新选课记录表，新增课程信息
     *
     * @param chooseCourseId 选课ID
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payChooseCourseSuccess(String chooseCourseId) {
        // 查询选课记录
        XcChooseCourse xcChooseCourse = xcChooseCourseService.getById(chooseCourseId);
        if (xcChooseCourse == null) {
            log.debug("接受购买课程的消息，根据选课ID从数据库找不到选课记录, 选课ID: {}", chooseCourseId);
            return false;
        }
        // 选课状态
        String status = xcChooseCourse.getStatus();
        if (status.equals("701001")) {
            // 已支付
            return false;
        }
        // 未支付，更新状态
        xcChooseCourse.setStatus("701001");
        boolean update = xcChooseCourseService.update(Wrappers.<XcChooseCourse>lambdaUpdate()
                .eq(XcChooseCourse::getId, chooseCourseId)
                .set(XcChooseCourse::getStatus, "701001"));
        if (!update) {
            log.debug("添加选课记录失败: {}", xcChooseCourse);
            throw new BizException("添加选课记录失败");
        }

        // 插入我的课程表
        xcCourseTablesService.addCourseTables(xcChooseCourse);
        return true;
    }

    /**
     * 查询我的课程表
     *
     * @param dto 查询参数
     */
    @Override
    public PageResult<XcCourseTables> getMyCourseTables(MyCourseTableParamsDTO dto) {
        String userId = dto.getUserId();
        int size = dto.getSize();
        int pageNo = dto.getPage();
        Page<XcCourseTables> iPage = new Page<>(pageNo, size);
        Page<XcCourseTables> pageResult = xcCourseTablesService.page(iPage, Wrappers.<XcCourseTables>lambdaQuery()
                .eq(XcCourseTables::getUserId, userId));
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageNo, size);
    }
}
