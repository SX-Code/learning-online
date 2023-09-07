package com.swx.content.service;

import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.content.model.dto.AddCourseDTO;
import com.swx.content.model.dto.EditCourseDTO;
import com.swx.content.model.dto.QueryCourseParamsDTO;
import com.swx.content.model.po.CourseBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.content.model.vo.CourseBaseInfoVO;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface CourseBaseService extends IService<CourseBase> {

    /**
     * 课程分页查询
     *
     * @param companyId 培训结构ID
     * @param pageParam 分页参数
     * @param dto       查询参数
     */
    public PageResult<CourseBase> queryCourseBaseList(Long companyId, PageParam pageParam, QueryCourseParamsDTO dto);

    /**
     * 新增课程
     *
     * @param companyId 机构ID
     * @param dto       课程信息
     */
    public CourseBaseInfoVO createCourseBase(Long companyId, AddCourseDTO dto);

    /**
     * 根据ID获取课程信息
     *
     * @param courseId 课程ID
     */
    public CourseBaseInfoVO getCourseBaseInfo(Long courseId);

    /**
     * 更新课程信息
     *
     * @param companyId 公司ID
     * @param dto 修改信息
     * @return 更新后的课程详细信息
     */
    CourseBaseInfoVO updateCourseBase(Long companyId, EditCourseDTO dto);
}
