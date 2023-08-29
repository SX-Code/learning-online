package com.swx.content.service.impl;

import com.swx.content.model.po.CourseCategory;
import com.swx.content.mapper.CourseCategoryMapper;
import com.swx.content.model.vo.CourseCategoryTreeVO;
import com.swx.content.service.CourseCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    /**
     * 查询课程分类信息
     *
     * @return 树形结构的分类信息
     */
    @Override
    public List<CourseCategoryTreeVO> treeNodes(String id) {
        // 查询数据
        List<CourseCategory> courseCategories = baseMapper.selectTreeNodes(id);
        // 将数据转换为VO形式
        List<CourseCategoryTreeVO> courseCategoryVos = courseCategories.stream().map(item -> {
            CourseCategoryTreeVO vo = new CourseCategoryTreeVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());

        // 转为Map<id, vo>的形式
        Map<String, CourseCategoryTreeVO> map = courseCategoryVos.stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toMap(CourseCategory::getId, value -> value, (key1, key2) -> key1));

        // 组装树形结构
        ArrayList<CourseCategoryTreeVO> results = new ArrayList<>();
        courseCategoryVos.stream()
                .filter(item -> !id.equals(item.getId()))
                .forEach(item -> {
                    if (item.getParentid().equals(id)) {
                        // 根节点
                        results.add(item);
                    }
                    // item的父节点
                    CourseCategoryTreeVO categoryTreeVO = map.get(item.getParentid());
                    if (categoryTreeVO != null) {
                        if (categoryTreeVO.getChildrenTreeNodes() == null) {
                            categoryTreeVO.setChildrenTreeNodes(new ArrayList<>());
                        }
                        // 添加到其父节点
                        categoryTreeVO.getChildrenTreeNodes().add(item);
                    }
                });
        return results;
    }
}
