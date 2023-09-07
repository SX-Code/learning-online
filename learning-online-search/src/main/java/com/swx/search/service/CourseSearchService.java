package com.swx.search.service;

import com.swx.base.model.PageParam;
import com.swx.search.dto.SearchCourseParamDTO;
import com.swx.search.vo.SearchPageResultVO;
import com.swx.search.po.CourseIndex;

/**
 * 课程搜索service
 */
public interface CourseSearchService {


    /**
     * 搜索课程列表
     *
     * @param pageParam 分页参数
     * @param dto        搜索条件
     * @return com.xuecheng.base.model.PageResult<com.swx.search.po.CourseIndex> 课程列表
     */
    SearchPageResultVO<CourseIndex> queryCoursePubIndex(PageParam pageParam, SearchCourseParamDTO dto);

}
