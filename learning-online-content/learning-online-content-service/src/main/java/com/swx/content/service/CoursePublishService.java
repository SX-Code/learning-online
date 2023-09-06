package com.swx.content.service;

import com.swx.content.model.po.CoursePublish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.content.model.vo.CoursePreviewVO;

import java.io.InputStream;

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

    /**
     * 提交审核
     * 将课程基本信息、课程计划信息和营销信息保存到预发布表
     *
     * @param companyId 机构ID
     * @param courseId  课程ID
     */
    public void commitAudit(Long companyId, Long courseId);

    /**
     * 发布课程
     *
     * @param companyId 机构ID
     * @param courseId  课程ID
     */
    public void publish(Long companyId, Long courseId);

    /**
     * 生成课程静态化页面
     *
     * @param courseId 课程ID
     * @return InputStream
     */
    public InputStream generateCourseHtml(Long courseId);

    /**
     * 上传课程静态化页面
     *
     * @param courseId    课程ID
     * @param inputStream 文件流
     */
    public void uploadCourseHtml(Long courseId, InputStream inputStream);

    /**
     * 查询已发布课程的信息
     *
     * @param courseId 课程ID
     * @return com.swx.content.model.po.CoursePublish 课程发布信息
     */
    public CoursePreviewVO getCoursePublish(Long courseId);

    /**
     * 查询已发布课程的信息（缓存）
     *
     * @param courseId 课程ID
     * @return com.swx.content.model.po.CoursePublish 课程发布信息
     */
    public CoursePreviewVO getCoursePublishCache(Long courseId);
}
