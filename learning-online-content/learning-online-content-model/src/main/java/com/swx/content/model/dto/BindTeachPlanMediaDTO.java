package com.swx.content.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BindTeachPlanMediaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 媒资文件ID
     */
    @NotBlank(message = "媒资文件不能为空")
    private String mediaId;

    /**
     * 媒资文件名称
     */
    private String fileName;

    /**
     * 课程计划标识
     */
    @NotNull(message = "课程计划信息不能为空")
    private Long teachplanId;
}
