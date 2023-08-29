package com.swx.media.api;


import com.swx.base.exception.ResponseResult;
import com.swx.base.model.R;
import com.swx.media.model.dto.UploadFileParamDTO;
import com.swx.media.model.vo.UploadFileResultVO;
import com.swx.media.service.MediaFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 媒资上传 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-08-21
 */
@Api(value = "媒资文件上传管理接口", tags = "媒资文件上传管理接口")
@RestController
@ResponseResult
@RequestMapping("/upload")
public class MediaFilesUploadController {

    private final MediaFilesService mediaFilesService;

    public MediaFilesUploadController(MediaFilesService mediaFilesService) {
        this.mediaFilesService = mediaFilesService;
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

    @ApiOperation("文件上传前的检查文件")
    @PostMapping("/checkfile")
    public Boolean checkFile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFilesService.checkFile(fileMd5);
    }

    @ApiOperation("分块文件上传前的检查文件")
    @PostMapping("/checkchunk")
    public Boolean checkChunk(@RequestParam("fileMd5") String fileMd5,
                              @RequestParam("chunk") int chunk) {
        return mediaFilesService.checkChunk(fileMd5, chunk);
    }

    @ApiOperation("上传分块文件")
    @PostMapping("/uploadchunk")
    public Boolean uploadChunk(@RequestParam("file") MultipartFile file,
                               @RequestParam("fileMd5") String fileMd5,
                               @RequestParam("chunk") int chunk) {
        return mediaFilesService.uploadChunk(file, fileMd5, chunk);
    }

    @ApiOperation("合并文件")
    @PostMapping("/mergechunks")
    public R mergeChunks(@RequestParam("fileName") String fileName,
                         @RequestParam("fileMd5") String fileMd5,
                         @RequestParam("chunkTotal") int chunkTotal) {
        Long companyId = 1232141425L;
        UploadFileParamDTO dto = new UploadFileParamDTO();
        dto.setFilename(fileName);
        dto.setFileType("001002");
        dto.setTags("视频文件");
        return mediaFilesService.mergeChunks(companyId, fileMd5, chunkTotal, dto);
    }

}

