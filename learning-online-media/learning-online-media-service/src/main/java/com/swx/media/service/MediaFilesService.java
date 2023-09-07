package com.swx.media.service;

import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.base.model.R;
import com.swx.media.model.dto.QueryMediaParamsDTO;
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
     * 查询所有符合条件的媒资信息
     *
     * @param companyId 机构ID
     * @param pageParam 分页参数
     * @param dto       查询参数
     * @return PageResult<MediaFiles>
     */
    PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParam pageParam, QueryMediaParamsDTO dto);

    /**
     * 上传文件
     *
     * @param companyId     机构ID
     * @param dto           文件信息
     * @param multipartFile 文件信息
     * @param objectName    文件路径
     * @return UploadFileResultVO
     */
    public UploadFileResultVO uploadFile(Long companyId, UploadFileParamDTO dto, MultipartFile multipartFile, String objectName);

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

    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件md5值
     * @return false不存在，true存在
     */
    Boolean checkFile(String fileMd5);

    /**
     * 检查文件分块是否存在
     *
     * @param fileMd5 文件md5
     * @param chunk   分块序号
     * @return false不存在，true存在
     */
    Boolean checkChunk(String fileMd5, int chunk);

    /**
     * 上传分块
     *
     * @param file    文件信息
     * @param fileMd5 文件md5
     * @param chunk   分块序号
     */
    Boolean uploadChunk(MultipartFile file, String fileMd5, int chunk);

    /**
     * 合并分块
     *
     * @param companyId  机构ID
     * @param fileMd5    文件md5
     * @param chunkTotal 分块数量
     * @param dto        文件信息
     */
    R mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamDTO dto);
}
