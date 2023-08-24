package com.swx.media.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryMediaParamsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String filename;
    private String fileType;
    private String auditStatus;

}
