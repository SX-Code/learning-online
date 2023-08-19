package com.swx.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.content.model.dto.QueryCourseParamsDTO;
import com.swx.content.model.po.CourseBase;
import com.swx.content.mapper.CourseBaseMapper;
import com.swx.content.service.CourseBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    /**
     * 课程分页查询
     *
     * @param pageParam 分页参数
     * @param dto       查询参数
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParam pageParam, QueryCourseParamsDTO dto) {
        LambdaQueryWrapper<CourseBase> wrapper = Wrappers.<CourseBase>lambdaQuery()
                .like(StringUtils.hasText(dto.getCourseName()), CourseBase::getName, dto.getCourseName())
                .eq(StringUtils.hasText(dto.getAuditStatus()), CourseBase::getAuditStatus, dto.getAuditStatus())
                .eq(StringUtils.hasText(dto.getPublishStatus()), CourseBase::getStatus, dto.getPublishStatus());

        Page<CourseBase> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        Page<CourseBase> pageResult = page(page, wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageParam);
    }
}
