package com.swx.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.swx.base.exception.BizException;
import com.swx.base.model.PageParam;
import com.swx.base.model.PageResult;
import com.swx.base.model.R;
import com.swx.media.config.MinIOConfigProperties;
import com.swx.media.model.dto.QueryMediaParamsDTO;
import com.swx.media.model.dto.UploadFileParamDTO;
import com.swx.media.model.enums.MediaFileStatusEnum;
import com.swx.media.model.po.MediaFiles;
import com.swx.media.mapper.MediaFilesMapper;
import com.swx.media.model.po.MediaProcess;
import com.swx.media.model.vo.UploadFileResultVO;
import com.swx.media.service.FileStorageService;
import com.swx.media.service.MediaFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.media.service.MediaProcessService;
import io.minio.ObjectStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    private final MediaProcessService mediaProcessService;
    private final MinIOConfigProperties minIOConfigProperties;

    public MediaFilesServiceImpl(FileStorageService fileStorageService, TransactionTemplate transactionTemplate,
                                 MediaProcessService mediaProcessService, MinIOConfigProperties minIOConfigProperties) {
        this.fileStorageService = fileStorageService;
        this.transactionTemplate = transactionTemplate;
        this.mediaProcessService = mediaProcessService;
        this.minIOConfigProperties = minIOConfigProperties;
    }

    /**
     * 查询所有符合条件的媒资信息
     *
     * @param companyId 机构ID
     * @param pageParam 分页参数
     * @param dto       查询参数
     * @return PageResult<MediaFiles>
     */
    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParam pageParam, QueryMediaParamsDTO dto) {
        IPage<MediaFiles> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        LambdaQueryWrapper<MediaFiles> wrapper = Wrappers.<MediaFiles>lambdaQuery()
                .like(StringUtils.hasText(dto.getFilename()), MediaFiles::getFilename, dto.getFilename())
                .eq(StringUtils.hasText(dto.getFileType()), MediaFiles::getFileType, dto.getFileType());
        IPage<MediaFiles> pageResult = page(page, wrapper);
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal(), pageParam);
    }

    /**
     * 上传文件
     *
     * @param companyId     机构ID
     * @param dto           文件信息
     * @param multipartFile 文件信息
     * @param objectName    文件路径
     * @return UploadFileResultVO
     */
    @Override
    public UploadFileResultVO uploadFile(Long companyId, UploadFileParamDTO dto, MultipartFile multipartFile, String objectName) {
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

            // 获取文章类型
            String mineType = getMineType(suffix);
            if (mineType.contains("image")) {
                dto.setFileType("001001");
            } else {
                dto.setFileType("001003");
            }

            // 上传到MinIO
            Map<String, String> result = null;
            if (StringUtils.hasText(objectName)) {
                result = fileStorageService.uploadMediaFile(objectName, mineType, multipartFile.getInputStream());
            } else {
                result = fileStorageService.uploadMediaFile("", prefix + suffix, mineType, multipartFile.getInputStream());
            }
            String bucket = result.get("bucket");
            String path = result.get("path");

            // 保存到数据库，使用编程式事务
            MediaFiles mediaFiles = transactionTemplate.execute(transactionStatus -> {
                return saveAfterStore(dto, companyId, prefix, bucket, path);
            });

            // 返回数据
            UploadFileResultVO resultVO = new UploadFileResultVO();
            if (mediaFiles == null) return resultVO;
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
    public MediaFiles saveAfterStore(UploadFileParamDTO dto, Long companyId, String fileMd5, String bucket, String path) {
        // 构造参数
        MediaFiles mediaFiles = new MediaFiles();
        BeanUtils.copyProperties(dto, mediaFiles);
        mediaFiles.setId(fileMd5);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setBucket(bucket);
        mediaFiles.setFilePath(path);
        mediaFiles.setFileId(fileMd5);
        mediaFiles.setUrl("/" + bucket + "/" + path);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setStatus(MediaFileStatusEnum.NORMAL.status());
        mediaFiles.setAuditStatus("002003");
        // 保存到媒资文件表
        boolean save = save(mediaFiles);
        if (!save) {
            log.error("想数据库保存文件失败，bucket: {}，文件名: {}", bucket, path);
            return null;
        }
        // 记录待处理任务
        addWaitingTask(mediaFiles);
        return mediaFiles;
    }

    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件md5值
     * @return false不存在，true存在
     */
    @Override
    public Boolean checkFile(String fileMd5) {
        MediaFiles dbMediaFile = getById(fileMd5);
        if (dbMediaFile != null) {
            // 文件存在数据库，查询MinIO
            ObjectStat objectStat = fileStorageService.getObjectStat(dbMediaFile.getBucket(), dbMediaFile.getFilePath());
            return objectStat != null;
        }
        return false;
    }

    /**
     * 检查文件分块是否存在
     *
     * @param fileMd5 文件md5
     * @param chunk   分块序号
     * @return false不存在，true存在
     */
    @Override
    public Boolean checkChunk(String fileMd5, int chunk) {
        // 分块存储路径：md5前两位为两个目录，chunk存储分块文件
        String path = getChunkFileFolderPath(fileMd5) + chunk;
        String bucket = minIOConfigProperties.getBucket().get("video");
        ObjectStat objectStat = fileStorageService.getObjectStat(bucket, path);
        return objectStat != null;
    }

    /**
     * 上传分块
     *
     * @param file    文件信息
     * @param fileMd5 文件md5
     * @param chunk   分块序号
     */
    @Override
    public Boolean uploadChunk(MultipartFile file, String fileMd5, int chunk) {
        try {
            String mineType = getMineType(null);
            String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;
            fileStorageService.uploadChunkFile(chunkFilePath, mineType, file.getInputStream());
            return true;
        } catch (IOException e) {
            log.error("分块文件上传失败，文件ID：{}，分块序号: {}", fileMd5, chunk, e);
            return false;
        }
    }

    /**
     * 合并分块
     *
     * @param companyId  机构ID
     * @param fileMd5    文件md5
     * @param chunkTotal 分块数量
     * @param dto   文件信息
     */
    @Override
    public R mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamDTO dto) {
        // 找到分块文件调用minio的sdk进行文件合并
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        String suffix = dto.getFilename().substring(dto.getFilename().lastIndexOf("."));
        String filepath = getFilePathByMd5(fileMd5, suffix);
        String bucket = minIOConfigProperties.getBucket().get("video");
        try {
            // 请求合并操作
            fileStorageService.mergeFile(chunkFileFolderPath, filepath, chunkTotal);
        } catch (Exception e) {
            log.error("文件合并失败，文件名: {}", filepath, e);
            return R.fail(false, "文件合并失败");
        }

        // 获取分片文件
        ObjectStat objectStat = fileStorageService.getObjectStat(bucket, filepath);
        if (objectStat == null) {
            return R.fail(false, "合并文件获取失败");
        }
        // 设置文件大小
        dto.setFileSize(objectStat.length());

        // 将文件信息入库
        // 保存到数据库，使用编程式事务
        MediaFiles mediaFiles = transactionTemplate.execute(transactionStatus -> {
            return saveAfterStore(dto, companyId, fileMd5, bucket, filepath);
        });
        if (mediaFiles == null) {
            return R.fail(false, "文件保存失败");
        }
        // 删除分块文件
        fileStorageService.clearChunkFiles(chunkFileFolderPath, chunkTotal);
        return R.success(true);
    }

    /**
     * 添加文件转码待处理任务
     * @param mediaFiles 文件信息
     */
    private void addWaitingTask(MediaFiles mediaFiles) {
        // 获取文件的mimeType, 如果是avi视频，写入待处理任务
        String filename = mediaFiles.getFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        String mineType = getMineType(suffix);
        if (!mineType.equals("video/x-msvideo")) {
            return;
        }
        MediaProcess mediaProcess = new MediaProcess();
        BeanUtils.copyProperties(mediaProcess, mediaFiles);
        mediaProcess.setStatus("1");
        mediaProcess.setCreateDate(LocalDateTime.now());
        mediaProcess.setFailCount(0);
        mediaProcess.setUrl(null);
        mediaProcessService.save(mediaProcess);
    }


    private String getMineType(String suffix) {
        if (suffix == null) suffix = "";
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

    /**
     * 得到合并后文件的路径
     *
     * @param fileMd5 源文件Md5
     * @param suffix  文件后缀
     * @return 分块存储路径
     */
    private String getFilePathByMd5(String fileMd5, String suffix) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + suffix;
    }

    /**
     * 分块存储路径：md5前两位为两个目录，chunk存储分块文件
     *
     * @param fileMd5 源文件Md5
     * @return 分块存储路径
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/chunk/";
    }
}
