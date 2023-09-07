package com.swx.learning.service;

import com.swx.base.model.R;

public interface LearningService {


    /**
     * 获取视频的学习资格
     *
     * @param userId      用户ID
     * @param courseId    课程ID
     * @param teachplanId 课程计划ID
     * @param mediaId     媒资ID
     */
    R getvideo(String userId, Long courseId, Long teachplanId, String mediaId);
}
