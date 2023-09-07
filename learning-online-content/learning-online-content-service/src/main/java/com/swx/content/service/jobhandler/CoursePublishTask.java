package com.swx.content.service.jobhandler;

import com.swx.base.exception.BizException;
import com.swx.content.client.SearchServiceClient;
import com.swx.content.model.po.CourseIndex;
import com.swx.content.model.po.CoursePublish;
import com.swx.content.model.vo.CoursePreviewVO;
import com.swx.content.service.CoursePublishService;
import com.swx.messagesdk.model.po.MqMessage;
import com.swx.messagesdk.service.MessageProcessAbstract;
import com.swx.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Slf4j
@Service
public class CoursePublishTask extends MessageProcessAbstract {

    private final CoursePublishService coursePublishService;
    private final SearchServiceClient searchServiceClient;

    public CoursePublishTask(CoursePublishService coursePublishService, SearchServiceClient searchServiceClient) {
        this.coursePublishService = coursePublishService;
        this.searchServiceClient = searchServiceClient;
    }

    @XxlJob("CoursePublishJobHandler")
    public void coursePublishJobHandler() {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        // 执行任务
        this.process(shardIndex, shardTotal, "course_publish", 30, 60);

    }

    /**
     * 任务处理
     *
     * @param mqMessage 执行任务内容
     * @return boolean true:处理成功，false处理失败
     */
    @Override
    public boolean execute(MqMessage mqMessage) {
        if (StringUtils.isEmpty(mqMessage.getBusinessKey1())) {
            log.debug("消息参数错误，无业务ID: {}", mqMessage);
            return false;
        }
        long courseId = Long.parseLong(mqMessage.getBusinessKey1());

        // 生成并上传静态页面
        generateCourseHtml(mqMessage, courseId);

        // 创建索引
        saveCourseIndex(mqMessage, courseId);

        return true;
    }

    /**
     * 生成静态化页面上传至文件系统
     *
     * @param mqMessage 消息
     * @param courseId  课程ID
     */
    private void generateCourseHtml(MqMessage mqMessage, long courseId) {
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        // 任务幂等性处理
        int stageOne = mqMessageService.getStageOne(taskId);
        if (stageOne > 0) {
            log.debug("静态化页面已生成，无需处理");
            return;
        }
        // 生成静态页面
        InputStream inputStream = coursePublishService.generateCourseHtml(courseId);
        if (inputStream == null) {
            return;
        }
        // 上传静态页面
        coursePublishService.uploadCourseHtml(courseId, inputStream);
        // 置状态为已完成
        mqMessageService.completedStageOne(taskId);

    }

    /**
     * 保存课程索引到Elasticsearch
     *
     * @param mqMessage 消息
     * @param courseId  课程ID
     */
    private void saveCourseIndex(MqMessage mqMessage, long courseId) {
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        // 任务幂等性处理
        int stageTwo = mqMessageService.getStageTwo(taskId);
        if (stageTwo > 0) {
            log.debug("课程索引已缓存，无需处理");
            return;
        }
        // 查询课程
        CoursePublish coursePublish = coursePublishService.getById(courseId);
        CourseIndex courseIndex = new CourseIndex();
        BeanUtils.copyProperties(coursePublish, courseIndex);
        // 远程调用
        Boolean add = searchServiceClient.add(courseIndex);
        if (!add) {
            throw new BizException("添加课程索引失败");
        }
        // 置状态为已完成
        mqMessageService.completedStageTwo(taskId);
    }
}
