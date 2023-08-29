package com.swx.content.service;

import com.swx.content.model.po.CourseMarket;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程营销信息 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface CourseMarketService extends IService<CourseMarket> {

    /**
     * 保存课程营销信息
     *
     * @param courseMarket 课程营销信息
     * @return 是否保存成功
     */
    public boolean saveOrUpdateCourseMarket(CourseMarket courseMarket);

}
