package com.swx.content.mapper;

import com.swx.content.model.po.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    public List<CourseCategory> selectTreeNodes(@Param("id") String id);

}
