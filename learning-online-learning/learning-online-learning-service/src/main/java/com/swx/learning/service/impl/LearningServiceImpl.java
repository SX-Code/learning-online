package com.swx.learning.service.impl;

import com.swx.base.model.R;
import com.swx.learning.client.ContentServerClient;
import com.swx.learning.client.MediaServerClient;
import com.swx.learning.model.dto.CoursePublish;
import com.swx.learning.model.vo.XcCourseTablesVO;
import com.swx.learning.service.LearningService;
import com.swx.learning.service.MyCourseTablesService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LearningServiceImpl implements LearningService {
    private final MyCourseTablesService myCourseTablesService;
    private final ContentServerClient contentServerClient;
    private final MediaServerClient mediaServerClient;

    public LearningServiceImpl(MyCourseTablesService myCourseTablesService, ContentServerClient contentServerClient, MediaServerClient mediaServerClient) {
        this.myCourseTablesService = myCourseTablesService;
        this.contentServerClient = contentServerClient;
        this.mediaServerClient = mediaServerClient;
    }

    /**
     * 获取视频的学习资格
     *
     * @param userId      用户ID
     * @param courseId    课程ID
     * @param teachplanId 课程计划ID
     * @param mediaId     媒资ID
     */
    @Override
    public R getvideo(String userId, Long courseId, Long teachplanId, String mediaId) {
        // 远程调用查询已发布课程
        CoursePublish coursePublish = contentServerClient.getCoursePublish(courseId);
        if (coursePublish == null) {
            return R.fail(-1, "课程不存在，无法预览");
        }

        // TODO 判断该视频视频所在课程小节是否支持试学



        if (StringUtils.isEmpty(userId)) {
            // 用户未登录
            if (coursePublish.getCharge().equals("201000")) {
                String url = mediaServerClient.getPlayUrlByMediaId(mediaId);
                return R.success(url);
            }
            return R.fail(-1, "请登陆后选课学习");
        }
        // 查询学习资格
        XcCourseTablesVO learningStatus = myCourseTablesService.getLearningStatus(userId, courseId);
        String learnStatus = learningStatus.getLearnStatus();
        if (learnStatus.equals("702002")) {
            return R.fail(-1, "无法学习，未选课或选课后没有支付");
        } else if (learnStatus.equals("702003")) {
            return R.fail(-1, "已过期需要申请续费或重新支付");
        }

        // 有资格, 远程调用媒资获取视频地址
        String url = mediaServerClient.getPlayUrlByMediaId(mediaId);
        return R.success(url);
    }
}
