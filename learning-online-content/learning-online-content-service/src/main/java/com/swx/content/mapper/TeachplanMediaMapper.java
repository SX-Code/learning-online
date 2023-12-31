package com.swx.content.mapper;

import com.swx.content.model.po.CoursePublishPre;
import com.swx.content.model.po.TeachPlanMedia;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface TeachplanMediaMapper extends BaseMapper<TeachPlanMedia> {

    /**
     * <p>
     * 课程发布 Mapper 接口
     * </p>
     *
     * @author sw-code
     * @since 2023-08-28
     */
    interface CoursePublishPreMapper extends BaseMapper<CoursePublishPre> {

    }
}
