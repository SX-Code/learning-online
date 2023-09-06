package com.swx.learning.model.vo;

import com.swx.learning.model.po.XcChooseCourse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class XcChooseCourseVO extends XcChooseCourse {
    // 学习资格
    public String learnStatus;
}
