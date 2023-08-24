package com.swx.media.service.impl;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.swx.base.exception.BizException;
import com.swx.base.model.ResultCodeEnum;
import com.swx.media.model.dto.UploadFileParamDTO;
import com.swx.media.model.enums.MediaFileStatusEnum;
import com.swx.media.model.po.MediaFiles;
import com.swx.media.mapper.MediaFilesMapper;
import com.swx.media.model.vo.UploadFileResultVO;
import com.swx.media.service.FileStorageService;
import com.swx.media.service.MediaFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-21
 */
@Slf4j
@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {

    private final FileStorageService fileStorageService;
    private final TransactionTemplate transactionTemplate;

    public MediaFilesServiceImpl(FileStorageService fileStorageService, TransactionTemplate transactionTemplate) {
        this.fileStorageService = fileStorageService;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * 上传文件
     *
     * @param companyId     机构ID
     * @param dto           文件信息
     * @param multipartFile 文件信息
     * @return UploadFileResultVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadFileResultVO uploadFile(Long companyId, UploadFileParamDTO dto, MultipartFile multipartFile) {

        String filename = dto.getFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        try {
            String prefix = getFileMd5(multipartFile.getInputStream());
            // 已存在数据库，直接返回
            MediaFiles dbMediaFile = getById(prefix);
            if (dbMediaFile != null) {
                UploadFileResultVO resultVO = new UploadFileResultVO();
                BeanUtils.copyProperties(dbMediaFile, resultVO);
                return resultVO;
            }
            // 上传到MinIO
            String mineType = getMineType(suffix);
            Map<String, String> result = fileStorageService.uploadMediaFile("", prefix + suffix, mineType, multipartFile.getInputStream());
            String bucket = result.get("bucket");
            String path = result.get("path");

            // 保存到数据库，使用编程式事务
            MediaFiles mediaFiles = transactionTemplate.execute(transactionStatus -> {
                return saveAfterStore(dto, companyId, prefix, bucket, path);
            });
            UploadFileResultVO resultVO = new UploadFileResultVO();
            BeanUtils.copyProperties(mediaFiles, resultVO);
            return resultVO;
        } catch (IOException e) {
            log.info("MediaFilesServiceImpl-上传文件失败", e);
            throw new BizException("文件上传失败");
        }
    }

    /**
     * 从dto保存到数据库
     *
     * @param dto 文件信息
     * @return 保是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MediaFiles saveAfterStore(UploadFileParamDTO dto, Long companyId, String prefix, String bucket, String path) {
        // 构造参数
        MediaFiles mediaFiles = new MediaFiles();
        BeanUtils.copyProperties(dto, mediaFiles);
        mediaFiles.setId(prefix);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setBucket(bucket);
        mediaFiles.setFilePath(path);
        mediaFiles.setFileId(prefix);
        mediaFiles.setUrl("/" + bucket + "/" + path);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setStatus(MediaFileStatusEnum.NORMAL.status());
        mediaFiles.setAuditStatus("002003");
        boolean save =  save(mediaFiles);
        if (!save) {
            throw new BizException(ResultCodeEnum.DATA_SAVE_FAIL);
        }
        return mediaFiles;
    }

    private String getMineType(String suffix) {
        if (suffix == null) return "";

        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(suffix);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    /**
     * 获得文件Md5值
     *
     * @param inputStream 文件
     * @return Md5值
     */
    private String getFileMd5(InputStream inputStream) {
        try {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
