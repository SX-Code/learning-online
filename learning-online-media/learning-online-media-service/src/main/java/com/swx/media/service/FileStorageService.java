package com.swx.media.service;

import io.minio.ObjectStat;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param prefix      文件前缀
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 桶和文件全路径
     */
    public Map<String, String> uploadMediaFile(String prefix, String filename, String mineType, InputStream inputStream);

    /**
     * 上传文件
     *
     * @param objectName  文件路径
     * @param inputStream 文件流
     * @return 桶和文件全路径
     */
    public Map<String, String> uploadMediaFile(String objectName, String mineType, InputStream inputStream);

    /**
     * 上传图片文件
     *
     * @param objectName  MinIO文件名
     * @param mineType    文件类型
     * @param inputStream 文件流
     */
    public boolean uploadVideoFile(String objectName, String mineType, InputStream inputStream);

    /**
     * 上传视频分块文件
     *
     * @param path        文件路径
     * @param inputStream 文件流
     * @return 文件全路径
     */
    public String uploadChunkFile(String path, String mimeType, InputStream inputStream);

    /**
     * 合并文件
     *
     * @param folder    分片目录路径
     * @param filepath  合并文件路径
     * @param chunkSize 分片数量
     */
    public void mergeFile(String folder, String filepath, int chunkSize) throws Exception;

    /**
     * 获取文件信息
     *
     * @param bucket   桶
     * @param filepath 文件路径
     * @return 文件信息
     */
    public ObjectStat getObjectStat(String bucket, String filepath);

    /**
     * 获取一个文件
     *
     * @param bucket 桶
     * @param path   路径
     * @return 文件流
     */
    public File downloadFile(String bucket, String path);

    /**
     * 清理分块文件
     *
     * @param chunkFolder 分块目录
     * @param chunkTotal  分块数量
     */
    void clearChunkFiles(String chunkFolder, int chunkTotal);
}
