package com.swx.content.service.impl;

import com.swx.content.model.po.CourseMarket;
import com.swx.content.mapper.CourseMarketMapper;
import com.swx.content.service.CourseMarketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程营销信息 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketMapper, CourseMarket> implements CourseMarketService {

    /**
     * 保存课程营销信息
     *
     * @param courseMarket 课程营销信息
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateCourseMarket(CourseMarket courseMarket) {
        String charge = courseMarket.getCharge();
        if (StringUtils.isEmpty(charge)) {
            throw new RuntimeException("收费规则为空");
        }
        if (charge.equals("201001")) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                throw new RuntimeException("课程的价格不能为空且必须大于0");
            }
        }
        CourseMarket dbMarket = getById(courseMarket.getId());
        if (dbMarket == null) {
            return save(courseMarket);
        } else {
            BeanUtils.copyProperties(courseMarket, dbMarket);
            dbMarket.setId(courseMarket.getId());
            return updateById(dbMarket);
        }
    }
}
