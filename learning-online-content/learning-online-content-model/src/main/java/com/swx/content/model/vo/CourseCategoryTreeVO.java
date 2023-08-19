package com.swx.content.model.vo;

import com.swx.content.model.po.CourseCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseCategoryTreeVO extends CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    List<CourseCategoryTreeVO> childrenTreeNodes;
}
