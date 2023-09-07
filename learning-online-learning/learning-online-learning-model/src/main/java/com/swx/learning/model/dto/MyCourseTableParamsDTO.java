package com.swx.learning.model.dto;

import lombok.Data;

@Data
public class MyCourseTableParamsDTO {
    String userId;
    private String courseType;
    private String sortType;
    private String expiresType;

    int page = 1;
    int startIndex;
    int size = 4;
}
