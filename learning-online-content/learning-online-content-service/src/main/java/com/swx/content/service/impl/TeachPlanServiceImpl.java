package com.swx.content.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.base.model.ResultCodeEnum;
import com.swx.content.mapper.TeachPlanMapper;
import com.swx.content.model.dto.BindTeachPlanMediaDTO;
import com.swx.content.model.dto.TeachPlanDTO;
import com.swx.content.model.po.TeachPlan;
import com.swx.content.model.po.TeachPlanMedia;
import com.swx.content.model.vo.TeachPlanVO;
import com.swx.content.service.TeachPlanMediaService;
import com.swx.content.service.TeachPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Service
public class TeachPlanServiceImpl extends ServiceImpl<TeachPlanMapper, TeachPlan> implements TeachPlanService {

    private final TeachPlanMediaService teachPlanMediaService;

    public TeachPlanServiceImpl(TeachPlanMediaService teachPlanMediaService) {
        this.teachPlanMediaService = teachPlanMediaService;
    }

    /**
     * 查询课程计划树形结构
     *
     * @param courseId 课程Id
     * @return 课程计划树形结构
     */
    @Override
    public List<TeachPlanVO> getTreeNodes(Long courseId) {
        return baseMapper.getTreeNodes(courseId);
    }

    /**
     * 新增或修改课程计划
     *
     * @param dto 课程计划
     */
    @Override
    public void saveTeachPlan(TeachPlanDTO dto) {
        Long teachPlanId = dto.getId();
        if (teachPlanId == null) {
            // 新增
            TeachPlan teachPlan = new TeachPlan();
            BeanUtils.copyProperties(dto, teachPlan);
            teachPlan.setCreateDate(LocalDateTime.now());
            // 排序字段
            int orderBy = count(Wrappers.<TeachPlan>lambdaQuery()
                    .eq(TeachPlan::getCourseId, dto.getCourseId())
                    .eq(TeachPlan::getParentid, dto.getParentid()));
            teachPlan.setOrderby(orderBy + 1);
            save(teachPlan);
            return;
        }
        // 修改
        TeachPlan teachPlan = getById(teachPlanId);
        if (teachPlan == null) {
            throw new BizException(ResultCodeEnum.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(dto, teachPlan);
        teachPlan.setChangeDate(LocalDateTime.now());
        updateById(teachPlan);
    }

    /**
     * 课程计划和媒资信息绑定
     *
     * @param dto 绑定信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeachPlanMedia associationMedia(BindTeachPlanMediaDTO dto) {
        TeachPlan dbTeachPlan = Optional.ofNullable(getById(dto.getTeachplanId())).orElseThrow(() -> new BizException(ResultCodeEnum.DATA_NOT_EXIST));
        if (dbTeachPlan.getGrade() != 2) {
            throw new BizException("只允许第二级教学计划绑定媒资文件");
        }
        // 删除原有记录，根据课程计划ID删除其绑定的媒资
        teachPlanMediaService.remove(Wrappers.<TeachPlanMedia>lambdaQuery().eq(TeachPlanMedia::getTeachplanId, dto.getTeachplanId()));
        // 新增记录
        TeachPlanMedia teachPlanMedia = new TeachPlanMedia();
        teachPlanMedia.setMediaId(dto.getMediaId());
        teachPlanMedia.setTeachplanId(dto.getTeachplanId());
        teachPlanMedia.setMediaFilename(dto.getFileName());
        teachPlanMedia.setCreateDate(LocalDateTime.now());
        teachPlanMedia.setCourseId(dbTeachPlan.getCourseId());
        teachPlanMediaService.save(teachPlanMedia);
        return teachPlanMedia;
    }
}
