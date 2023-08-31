package com.swx.content.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

/**
 * 传输文件支持
 */
@Configuration
public class MultipartSupportConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public MultipartSupportConfig(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    @Primary//注入相同类型的bean时优先使用
    @Scope("prototype")
    public Encoder feignEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * 将file转为Multipart
     *
     * @param file 文件
     * @return MultipartFile
     */
    public static MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
        try (FileInputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = item.getOutputStream();) {
            IOUtils.copy(inputStream, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonsMultipartFile(item);
    }

    /**
     * 将 输入流 转为Multipart
     *
     * @param inputStream 输入流
     * @param filename    文件名
     * @return MultipartFile
     */
    public static MultipartFile getMultipartFile(InputStream inputStream, String filename) {
        FileItem item = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, filename);
        try (OutputStream outputStream = item.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new CommonsMultipartFile(item);
    }
}
