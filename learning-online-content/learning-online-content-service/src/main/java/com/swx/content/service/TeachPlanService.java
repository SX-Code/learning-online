package com.swx.content.service;

import com.swx.content.model.dto.TeachPlanDTO;
import com.swx.content.model.po.TeachPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.content.model.vo.TeachPlanVO;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface TeachPlanService extends IService<TeachPlan> {

    /**
     * 查询课程计划树形结构
     *
     * @param courseId 课程Id
     * @return 课程计划树形结构
     */
    public List<TeachPlanVO> getTreeNodes(Long courseId);

    /**
     * 新增或修改课程计划
     *
     * @param dto 课程计划
     */
    public void saveTeachPlan(TeachPlanDTO dto);

}
