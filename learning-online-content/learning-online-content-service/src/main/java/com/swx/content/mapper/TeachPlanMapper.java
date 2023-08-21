package com.swx.content.mapper;

import com.swx.content.model.po.TeachPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.content.model.vo.TeachPlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface TeachPlanMapper extends BaseMapper<TeachPlan> {

    /**
     * 查询课程计划
     *
     * @param courseId 课程Id
     * @return 课程计划
     */
    public List<TeachPlanVO> getTreeNodes(@Param("courseId") Long courseId);

}
