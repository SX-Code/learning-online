package com.swx.content.model.vo;

import com.swx.content.model.po.TeachPlan;
import com.swx.content.model.po.TeachPlanMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 课程计划信息模型类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeachPlanVO extends TeachPlan {

    private TeachPlanMedia teachplanMedia;
    private List<TeachPlanVO> teachPlanTreeNodes;

}
