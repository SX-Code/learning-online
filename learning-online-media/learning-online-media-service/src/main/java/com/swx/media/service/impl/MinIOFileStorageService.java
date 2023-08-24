package com.swx.media.service.impl;

import com.swx.media.config.MinIOConfig;
import com.swx.media.config.MinIOConfigProperties;
import com.swx.media.service.FileStorageService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@EnableConfigurationProperties(MinIOConfigProperties.class)
@Import(MinIOConfig.class)
@Service
public class MinIOFileStorageService implements FileStorageService {

    private final MinioClient minioClient;
    private final MinIOConfigProperties minIOConfigProperties;
    private final static String separator = "/";

    public MinIOFileStorageService(MinioClient minioClient, MinIOConfigProperties minIOConfigProperties) {
        this.minioClient = minioClient;
        this.minIOConfigProperties = minIOConfigProperties;
    }

    /**
     * @param dirPath
     * @param filename yyyy/mm/dd/file.jpg
     * @return
     */
    public String builderFilePath(String dirPath, String filename) {
        StringBuilder stringBuilder = new StringBuilder(50);
        if (!StringUtils.isEmpty(dirPath)) {
            stringBuilder.append(dirPath).append(separator);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = sdf.format(new Date());
        stringBuilder.append(todayStr).append(separator);
        stringBuilder.append(filename);
        return stringBuilder.toString();
    }

    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public Map<String, String> uploadMediaFile(String prefix, String filename, String mineType, InputStream inputStream) {
        String filepath = builderFilePath(prefix, filename);
        String bucket = minIOConfigProperties.getBucket().get("files");
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filepath)
                    .contentType(mineType)
                    .bucket(bucket)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            Map<String, String> map = new HashMap<>();
            map.put("path", filepath);
            map.put("bucket", bucket);
            return map;
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 上传视频分块文件
     *
     * @param path        文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadChunkFile(String path, String mimeType, InputStream inputStream) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(path)
                    .bucket(minIOConfigProperties.getBucket().get("video"))
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(mimeType)
                    .build();
            minioClient.putObject(putObjectArgs);
            return path;
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 合并文件
     *
     * @param folder    分片目录路径
     * @param filepath  合并文件路径
     * @param chunkSize 分片数量
     * @return bucket
     */
    @Override
    public void mergeFile(String folder, String filepath, int chunkSize) throws Exception {
        try {
            String bucket = minIOConfigProperties.getBucket().get("video");
            // 源文件
            List<ComposeSource> sources = Stream.iterate(0, i -> ++i).limit(chunkSize)
                    .map(i -> ComposeSource.builder().bucket(bucket).object(folder + i).build()).collect(Collectors.toList());
            // 合并
            ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                    .object(filepath)
                    .bucket(bucket)
                    .sources(sources)
                    .build();
            minioClient.composeObject(composeObjectArgs);
        } catch (Exception ex) {
            log.error("minio compose file error.", ex);
            throw new Exception("合并文件失败");
        }
    }

    /**
     * 获取文件信息
     *
     * @param bucket   桶
     * @param filepath 文件路径
     * @return 文件信息
     */
    @Override
    public ObjectStat getObjectStat(String bucket, String filepath) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder().bucket("video").object(filepath).build());
        } catch (Exception e) {
            log.error("文件信息获取失败, 桶: {}, 文件路径: {}", bucket, filepath, e);
            return null;
        }
    }

    /**
     * 获取一个文件
     *
     * @param bucket 桶
     * @param path   文件路径
     */
    @Override
    public File downloadFile(String bucket, String path) {
        FileOutputStream outputStream = null;
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .build();
            InputStream ioStream = minioClient.getObject(getObjectArgs);
            File minioTempFile = File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioTempFile);
            IOUtils.copy(ioStream, outputStream);
            return minioTempFile;
        } catch (Exception ex) {
            log.error("minio compose file error.", ex);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("文件输出流关闭失败", e);
                }
            }
        }
        return null;
    }

    /**
     * 清理分块文件
     *
     * @param chunkFolder 分块目录
     * @param chunkTotal  分块数量
     */
    @Override
    @Async
    public void clearChunkFiles(String chunkFolder, int chunkTotal) {
        // 构建清理item
        List<DeleteObject> objects = Stream.iterate(0, i -> ++i).limit(chunkTotal)
                .map(i -> new DeleteObject(chunkFolder.concat(Integer.toString(i)))).collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(minIOConfigProperties.getBucket().get("video"))
                .objects(objects)
                .build();
        // 清理文件
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        // 需要迭代返回的可迭代来执行删除。
        for (Result<DeleteError> result : results) {
            DeleteError error = null;
            try {
                error = result.get();
                log.info("Error in deleting object {}; {}", error.objectName(), error.message());
            } catch (Exception e) {
                log.error("清理文件出错", e);
            }

        }
    }
}
