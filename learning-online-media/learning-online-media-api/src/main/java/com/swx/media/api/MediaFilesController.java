package com.swx.media.api;


import com.swx.base.exception.ResponseResult;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.media.model.po.MediaFiles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-08-21
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
@ResponseResult
@RequestMapping("/files")
public class MediaFilesController {

    @ApiOperation("媒资列表查询接口")
    @PostMapping("")
    public PageResult<MediaFiles> list(PageParam pageParam) {
        Long companyId = 1232141425L;
        return null;
    }

}

