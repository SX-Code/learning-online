package com.swx.content.service;

import com.swx.content.model.po.CoursePublish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.content.model.vo.CoursePreviewVO;

/**
 * <p>
 * 课程发布 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface CoursePublishService extends IService<CoursePublish> {

    /**
     * 获取课程预览信息
     *
     * @param courseId 课程ID
     * @return CoursePreviewVO 预览信息
     */
    public CoursePreviewVO getCoursePreviewInfo(Long courseId);

}
