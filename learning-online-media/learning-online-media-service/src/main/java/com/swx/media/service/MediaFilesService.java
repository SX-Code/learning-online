package com.swx.media.service;

import com.swx.media.model.dto.UploadFileParamDTO;
import com.swx.media.model.po.MediaFiles;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.media.model.vo.UploadFileResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-21
 */
public interface MediaFilesService extends IService<MediaFiles> {

    /**
     * 上传文件
     *
     * @param companyId     机构ID
     * @param dto           文件信息
     * @param multipartFile 文件信息
     * @return UploadFileResultVO
     */
    public UploadFileResultVO uploadFile(Long companyId, UploadFileParamDTO dto, MultipartFile multipartFile);

    /**
     * 从dto保存到数据库
     *
     * @param dto 文件信息
     * @return 保是否保存成功
     */
    /**
     * 上传到MinIO之后保存到数据库
     *
     * @param dto       文件信息
     * @param companyId 机构ID
     * @param prefix    前缀
     * @param bucket    桶
     * @param path      文件路径
     * @return 保是否保存成功
     */
    public MediaFiles saveAfterStore(UploadFileParamDTO dto, Long companyId, String prefix, String bucket, String path);

}
