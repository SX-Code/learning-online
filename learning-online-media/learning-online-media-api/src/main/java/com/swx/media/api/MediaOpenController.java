package com.swx.media.api;

import com.swx.base.exception.BizException;
import com.swx.base.exception.ResponseResult;
import com.swx.base.model.R;
import com.swx.media.model.po.MediaFiles;
import com.swx.media.service.MediaFilesService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 视频播放页面，openapi
 */

@RestController
@ResponseResult
@RequestMapping("/open")
public class MediaOpenController {

    private final MediaFilesService mediaFilesService;

    public MediaOpenController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @GetMapping("/preview/{mediaId}")
    public R getPlayUrlByMediaId(@PathVariable("mediaId") String mediaId) {
        MediaFiles mediaFiles = mediaFilesService.getById(mediaId);
        if (mediaFiles == null || StringUtils.isEmpty(mediaFiles.getUrl())) {
            throw new BizException("视频还没有转码处理");
        }
        return R.success(mediaFiles.getUrl());
    }
}
