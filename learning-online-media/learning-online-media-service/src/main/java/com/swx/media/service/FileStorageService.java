package com.swx.media.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

public interface FileStorageService {

    /**
     *  上传文件
     * @param prefix  文件前缀
     * @param filename  文件名
     * @param inputStream 文件流
     * @return  桶和文件全路径
     */
    public Map<String, String> uploadMediaFile(String prefix, String filename, String mineType, InputStream inputStream);

    /**
     *  上传视频
     * @param prefix  文件前缀
     * @param filename   文件名
     * @param inputStream  文件流
     * @return  文件全路径
     */
    public String uploadHtmlFile(String prefix, String filename,InputStream inputStream);
}
