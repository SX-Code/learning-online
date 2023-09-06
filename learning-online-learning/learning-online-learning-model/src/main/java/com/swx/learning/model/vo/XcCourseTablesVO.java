package com.swx.learning.model.vo;

import com.swx.learning.model.po.XcCourseTables;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class XcCourseTablesVO extends XcCourseTables {
    // 学习资格
    public String learnStatus;
}
