package com.swx.media.model.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UploadFileParamDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件类型（图片、文档，视频）
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人
     */
    private String username;

    /**
     * 备注
     */
    private String remark;
}
