package com.swx.content.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.content.client.MediaServiceClient;
import com.swx.content.config.MultipartSupportConfig;
import com.swx.content.model.po.*;
import com.swx.content.mapper.CoursePublishMapper;
import com.swx.content.model.vo.CourseBaseInfoVO;
import com.swx.content.model.vo.CoursePreviewVO;
import com.swx.content.model.vo.TeachPlanVO;
import com.swx.content.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.messagesdk.service.MqMessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {

    private final static String COURSE_TEMPLATE = "course_template";
    private final static String HTML_PATH = "course/";

    private final CourseBaseService courseBaseService;
    private final TeachPlanService teachPlanService;
    private final CourseMarketService courseMarketService;
    private final CourseCategoryService courseCategoryService;
    private final CoursePublishPreService coursePublishPreService;
    private final MqMessageService mqMessageService;
    private final MediaServiceClient mediaServiceClient;
    private final Configuration configuration;

    public CoursePublishServiceImpl(CourseBaseService courseBaseService, TeachPlanService teachPlanService,
                                    CourseMarketService courseMarketService, CourseCategoryService courseCategoryService,
                                    CoursePublishPreService coursePublishPreService, MqMessageService mqMessageService,
                                    MediaServiceClient mediaServiceClient, Configuration configuration) {
        this.courseBaseService = courseBaseService;
        this.teachPlanService = teachPlanService;
        this.courseMarketService = courseMarketService;
        this.courseCategoryService = courseCategoryService;
        this.coursePublishPreService = coursePublishPreService;
        this.mqMessageService = mqMessageService;
        this.mediaServiceClient = mediaServiceClient;
        this.configuration = configuration;
    }

    /**
     * 获取课程预览信息
     *
     * @param courseId 课程ID
     * @return CoursePreviewVO 预览信息
     */
    @Override
    public CoursePreviewVO getCoursePreviewInfo(Long courseId) {
        // 课程基本信息, 营销信息
        CourseBaseInfoVO courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        // 课程计划信息
        List<TeachPlanVO> teachPlans = teachPlanService.getTreeNodes(courseId);
        // 封装VO
        CoursePreviewVO previewVO = new CoursePreviewVO();
        previewVO.setCourseBase(courseBaseInfo);
        previewVO.setTeachplans(teachPlans);
        return previewVO;
    }

    /**
     * 提交审核
     * 将课程基本信息、课程计划信息和营销信息保存到预发布表
     *
     * @param companyId 机构ID
     * @param courseId  课程ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commitAudit(Long companyId, Long courseId) {
        // 查询课程基本信息
        CourseBase courseBase = Optional.ofNullable(courseBaseService.getById(courseId)).orElseThrow(() -> new BizException("课程信息不存在"));
        // 如果课程的审核状态为已提交则不允许提交
        if ("202003".equals(courseBase.getAuditStatus())) {
            throw new BizException("当前为等待审核状态，审核完成可以再次提交");
        }
        if (!courseBase.getCompanyId().equals(companyId)) {
            throw new BizException("不允许提交其它机构的课程");
        }
        // 课程的图片
        if (StringUtils.isEmpty(courseBase.getPic())) {
            throw new BizException("提交失败，请上传课程图片");
        }

        // 查询课程计划信息
        List<TeachPlanVO> teachPlans = teachPlanService.getTreeNodes(courseId);
        // 计划信息没有填写也不允许提交
        if (teachPlans.isEmpty()) {
            throw new BizException("提交失败，还没有添加课程计划");
        }

        // 查询营销信息
        CourseMarket courseMarket = Optional.ofNullable(courseMarketService.getById(courseId)).orElseThrow(() -> new BizException("请完善课程营销信息"));

        // 查询分类信息
        List<String> list = Arrays.asList(courseBase.getSt(), courseBase.getMt());
        String join = StringUtils.join(list, ",");
        List<CourseCategory> categories = courseCategoryService.list(Wrappers.<CourseCategory>lambdaQuery()
                .in(CourseCategory::getId, list).last("ORDER BY FIELD(id, '" + list.get(0) + "', '" + list.get(1) + "')"));

        // 查询课程基本信息、营销信息、计划等信息插入到课程预发布表
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        // 设置分类名
        coursePublishPre.setStName(categories.get(0) == null ? "" : categories.get(0).getName());
        coursePublishPre.setMtName(categories.get(1) == null ? "" : categories.get(1).getName());
        BeanUtils.copyProperties(courseMarket, coursePublishPre);
        // 设置基本信息
        BeanUtils.copyProperties(courseBase, coursePublishPre);
        // 设置营销信息
        coursePublishPre.setMarket(JSON.toJSONString(courseMarket));
        // 设置课程计划
        coursePublishPre.setTeachplan(JSON.toJSONString(teachPlans));

        //设置预发布记录状态,已提交
        coursePublishPre.setStatus("202003");
        //教学机构id
        coursePublishPre.setCompanyId(companyId);
        //提交时间
        coursePublishPre.setCreateDate(LocalDateTime.now());

        // 保存预发布信息
        CoursePublishPre coursePublishPreUpdate = coursePublishPreService.getById(courseId);
        if (coursePublishPreUpdate == null) {
            //添加课程预发布记录
            coursePublishPreService.save(coursePublishPre);
        } else {
            coursePublishPreService.updateById(coursePublishPre);
        }

        //更新课程基本表的审核状态
        courseBase.setAuditStatus("202003");
        courseBaseService.updateById(courseBase);

    }

    /**
     * 发布课程
     *
     * @param companyId 机构ID
     * @param courseId  课程ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long companyId, Long courseId) {
        // 查询预发布表
        CoursePublishPre coursePublishPre = Optional.ofNullable(coursePublishPreService.getById(courseId))
                .orElseThrow(() -> new BizException("未提交审核，不允许发布"));

        if (!coursePublishPre.getCompanyId().equals(companyId)) {
            throw new BizException("不允许发布其它机构的课程");
        }
        if (!coursePublishPre.getStatus().equals("202004")) {
            // 未通过审核
            throw new BizException("未通过审核，不允许发布");
        }
        // 向课程发布表写数据
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        // 查询是否有发布记录
        CoursePublish dbCoursePublish = getById(courseId);
        if (dbCoursePublish == null) {
            // 没有则新增
            save(coursePublish);
        } else {
            // 有则更新
            updateById(coursePublish);
        }

        // 更改文章状态为已经发布
        courseBaseService.update(Wrappers.<CourseBase>lambdaUpdate().eq(CourseBase::getId, courseId).set(CourseBase::getStatus, "203002"));

        // 向消息表写入数据
        saveCoursePublishMessage(courseId);

        // 删除预发布表数据
        coursePublishPreService.removeById(coursePublishPre);
    }

    /**
     * 生成课程静态化页面
     *
     * @param courseId 课程ID
     * @return File
     */
    @Override
    public InputStream generateCourseHtml(Long courseId) {
        try {
            Template template = configuration.getTemplate(COURSE_TEMPLATE + ".ftl");
            StringWriter out = new StringWriter();
            // 数据模型
            HashMap<String, Object> params = new HashMap<>();
            params.put("model", getCoursePreviewInfo(courseId));
            // 文章内容通过freemarker生成html文件
            template.process(params, out);
            // 返回文件流
            return new ByteArrayInputStream(out.toString().getBytes());
        } catch (Exception e) {
            log.error("页面静态化出现问题", e);
        }
        return null;
    }

    /**
     * 上传课程静态化页面
     *
     * @param courseId    课程ID
     * @param inputStream 文件流
     */
    @Override
    public void uploadCourseHtml(Long courseId, InputStream inputStream) {
        // 把html文件上传到minio中
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(inputStream, courseId + COURSE_TEMPLATE + ".html");
        String upload = mediaServiceClient.upload(multipartFile, HTML_PATH + courseId + ".html");
        if (upload == null) {
            log.debug("上传结果为null, 课程ID: {}", courseId);
        }
    }

    /**
     * 保存消息记录
     *
     * @param courseId 课程id
     */
    public void saveCoursePublishMessage(Long courseId) {
        Optional.ofNullable(mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null))
                .orElseThrow(() -> new BizException("保存消息记录失败"));
    }
}
