package com.swx.content.api;

import com.swx.base.exception.ResponseResult;
import com.swx.content.model.dto.BindTeachPlanMediaDTO;
import com.swx.content.model.dto.TeachPlanDTO;
import com.swx.content.model.po.TeachPlanMedia;
import com.swx.content.model.vo.TeachPlanVO;
import com.swx.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程计划信息 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-08-20
 */
@Api(value = "课程计划信息编辑接口", tags = "课程计划信息编辑接口")
@RestController
@ResponseResult
@RequestMapping("/teachplan")
public class TeachPlanController {

    private final TeachPlanService teachPlanService;

    public TeachPlanController(TeachPlanService teachPlanService) {
        this.teachPlanService = teachPlanService;
    }

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachPlanVO> getTreeNodes(@PathVariable("courseId") Long courseId) {
        return teachPlanService.getTreeNodes(courseId);
    }

    @ApiOperation("创建或修改课程计划")
    @PostMapping("")
    public void saveTeachPlan(@RequestBody TeachPlanDTO dto) {
        teachPlanService.saveTeachPlan(dto);
    }

    @ApiOperation("课程计划和媒资信息绑定")
    @PostMapping("/association/media")
    public TeachPlanMedia associateMedia(@RequestBody @Validated BindTeachPlanMediaDTO dto) {
        return teachPlanService.associationMedia(dto);
    }
}
