package com.swx.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询参数
 */
@Data
@ApiModel("分页查询参数")
public class PageParam {

    // 当前页
    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;
    // 每页记录数
    @ApiModelProperty("每页记录数")
    private Long pageSize = 10L;

    public PageParam() {
    }

    public PageParam(Long pageNo, Long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
