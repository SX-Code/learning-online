package com.swx.media;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.StatObjectArgs;
import io.minio.errors.*;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIOTest {

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://124.221.23.47:9000")
                    .credentials("minio", "minio123")
                    .build();

    @Test
    public void getFileStat() {
        String filepath = "5/0/5068e1e23619d41eb7e1c637537e51be/5068e1e23619d41eb7e1c637537e51be.mp4";
        // Get information of an object.
        try {
            ObjectStat objectStat = objectStat = minioClient.statObject(
                    StatObjectArgs.builder().bucket("video").object(filepath).build());
            System.out.println(objectStat);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件获取失败");
        }
    }
}
