package com.swx.media.api;

import com.swx.base.exception.ResponseResult;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.media.model.dto.QueryMediaParamsDTO;
import com.swx.media.model.po.MediaFiles;
import com.swx.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    private final MediaFilesService mediaFilesService;

    public MediaFilesController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @ApiOperation("媒资列表查询接口")
    @PostMapping
    public PageResult<MediaFiles> list(PageParam pageParam, @RequestBody QueryMediaParamsDTO dto) {
        Long companyId = 1232141425L;
        return mediaFilesService.queryMediaFiles(companyId, pageParam, dto);
    }

    @GetMapping("/video/url/{mediaId}")
    public String getPlayUrlByMediaId(@PathVariable("mediaId") String mediaId) {
        MediaFiles mediaFiles = mediaFilesService.getById(mediaId);
        return mediaFiles.getUrl();
    }
}
