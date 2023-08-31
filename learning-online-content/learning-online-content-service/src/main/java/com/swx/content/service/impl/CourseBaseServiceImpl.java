package com.swx.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.base.exception.BizException;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.base.model.ResultCodeEnum;
import com.swx.content.model.dto.AddCourseDTO;
import com.swx.content.model.dto.EditCourseDTO;
import com.swx.content.model.dto.QueryCourseParamsDTO;
import com.swx.content.model.po.CourseBase;
import com.swx.content.mapper.CourseBaseMapper;
import com.swx.content.model.po.CourseCategory;
import com.swx.content.model.po.CourseMarket;
import com.swx.content.model.vo.CourseBaseInfoVO;
import com.swx.content.service.CourseBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.content.service.CourseCategoryService;
import com.swx.content.service.CourseMarketService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

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

    private final CourseMarketService courseMarketService;
    private final CourseCategoryService courseCategoryService;

    public CourseBaseServiceImpl(CourseMarketService courseMarketService, CourseCategoryService courseCategoryService) {
        this.courseMarketService = courseMarketService;
        this.courseCategoryService = courseCategoryService;
    }

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

    /**
     * 新增课程
     *
     * @param companyId 机构ID
     * @param dto       课程信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseBaseInfoVO createCourseBase(Long companyId, AddCourseDTO dto) {

        // 新增course_base数据
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(dto, courseBase);

        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        // TODO 添加创建人信息

        courseBase.setStatus("203001");
        courseBase.setAuditStatus("202002");
        boolean save = save(courseBase);
        if (!save) {
            throw new BizException("添加课程失败");
        }

        // 新增course_market数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        courseMarket.setId(courseBase.getId());
        courseMarketService.saveOrUpdateCourseMarket(courseMarket);

        // 查询详细信息
        return getCourseBaseInfo(courseMarket.getId());
    }

    /**
     * 查询课程信息
     *
     * @param courseId 课程ID
     * @return 课程信息
     */
    @Override
    public CourseBaseInfoVO getCourseBaseInfo(Long courseId) {
        // 查询基本信息
        CourseBase courseBase = getById(courseId);
        if (courseBase == null) {
            return null;
        }
        // 查询营销信息
        CourseMarket courseMarket = Optional.ofNullable(courseMarketService.getById(courseId)).orElse(new CourseMarket());
        CourseBaseInfoVO courseBaseInfoVO = new CourseBaseInfoVO();
        BeanUtils.copyProperties(courseMarket, courseBaseInfoVO);
        BeanUtils.copyProperties(courseBase, courseBaseInfoVO);

        // 查询分类信息
        CourseCategory mtCategory = courseCategoryService.getById(courseBase.getMt());
        CourseCategory stCategory = courseCategoryService.getById(courseBase.getSt());
        courseBaseInfoVO.setMtName(mtCategory == null ? "" : mtCategory.getName());
        courseBaseInfoVO.setStName(stCategory == null ? "" : stCategory.getName());
        return courseBaseInfoVO;
    }

    /**
     * 更新课程信息
     *
     * @param companyId 公司ID
     * @param dto       修改信息
     * @return 更新后的课程详细信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseBaseInfoVO updateCourseBase(Long companyId, EditCourseDTO dto) {
        CourseBase dbCourseBase = getById(dto.getId());
        if (dbCourseBase == null) {
            throw new BizException(ResultCodeEnum.DATA_NOT_EXIST);
        }

        if (!companyId.equals(dbCourseBase.getCompanyId())) {
            throw new BizException("本机构只能修改本机构的课程");
        }

        BeanUtils.copyProperties(dto, dbCourseBase);
        dbCourseBase.setChangeDate(LocalDateTime.now());
        // TODO 添加修改人信息

        // 更新课程信息
        boolean updateBase = updateById(dbCourseBase);
        // 更新营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        boolean updateMarket = courseMarketService.saveOrUpdateCourseMarket(courseMarket);

        if (!updateBase || !updateMarket) {
            throw new BizException("修改课程失败");
        }
        return getCourseBaseInfo(dbCourseBase.getId());
    }
}
