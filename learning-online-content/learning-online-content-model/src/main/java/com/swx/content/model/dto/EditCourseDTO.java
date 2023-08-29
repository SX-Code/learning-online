package com.swx.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class EditCourseDTO extends AddCourseDTO {

    @ApiModelProperty("课程ID")
    @NotNull(message = "无法查询未知课程")
    private Long id;
}
