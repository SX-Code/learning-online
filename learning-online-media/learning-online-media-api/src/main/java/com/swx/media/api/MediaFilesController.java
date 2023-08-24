package com.swx.media.api;


import com.swx.base.exception.ResponseResult;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.media.model.dto.UploadFileParamDTO;
import com.swx.media.model.po.MediaFiles;
import com.swx.media.model.vo.UploadFileResultVO;
import com.swx.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/upload")
public class MediaFilesController {

    private final MediaFilesService mediaFilesService;

    public MediaFilesController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
    }

    @ApiOperation("媒资列表查询接口")
    @PostMapping("")
    public PageResult<MediaFiles> list(PageParam pageParam) {
        Long companyId = 1232141425L;
        return null;
    }

    @ApiOperation("上传文件")
    @PostMapping(value = "/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultVO uploadImage(@RequestPart("filedata") MultipartFile filedata) {
        Long companyId = 1232141425L;
        UploadFileParamDTO dto = new UploadFileParamDTO();
        dto.setFilename(filedata.getOriginalFilename());
        dto.setFileSize(filedata.getSize());
        dto.setFileType("001001");
        return mediaFilesService.uploadFile(companyId, dto, filedata);
    }

}

