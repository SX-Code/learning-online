package com.swx.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果模型类
 *
 * @param <T> 范型
 */
@Data
public class PageResult<T> implements Serializable {

    // 数据列表
    private List<T> items;
    // 总记录数
    private long counts;
    // 当前页数
    private long page;
    // 每页记录数
    private long pageSize;

    public PageResult() {
    }

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageResult(List<T> items, long counts, PageParam pageParam) {
        this.items = items;
        this.counts = counts;
        this.page = pageParam.getPageNo();
        this.pageSize = pageParam.getPageSize();
    }
}
