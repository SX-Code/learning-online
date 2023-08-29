package com.swx.content.service;

import com.swx.content.model.po.CourseCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.content.model.vo.CourseCategoryTreeVO;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    /**
     * 查询课程分类信息
     *
     * @return 树形结构的分类信息
     */
    List<CourseCategoryTreeVO> treeNodes(String id);
}
