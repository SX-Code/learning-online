package com.swx.media;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 测试大文件分片上传
 */
public class BigFileTest {

    @Test
    public void testChunk() throws IOException {
        // 源文件
        File sourceFile = new File("/Users/swcode/Downloads/sparkle_your_name_am720p.mp4");
        // 分块之后的存储路径
        String chunkFolder = "/Users/swcode/Downloads/chunk/";
        // 分块文件大小
        int chunkSize = 1024 * 1024 * 5;
        // 分块文件个数
        int chunNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        // 使用流从源文件读数据，向分块文件中写数据
        RandomAccessFile raf_r = new RandomAccessFile(sourceFile, "r");

        byte[] bytes = new byte[1024];
        for (int i = 0; i < chunNum; i++) {
            File chunkFile = new File(chunkFolder + i);
            // 分块文件写入流
            RandomAccessFile raf_rw = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_r.read(bytes)) != -1) {
                raf_rw.write(bytes, 0, len);
                if (chunkFile.length() >= chunkSize) {
                    break;
                }
            }
            raf_rw.close();
        }
        raf_r.close();
    }

    @Test
    public void testMerge() throws IOException {
        // 源文件
        File sourceFile = new File("/Users/swcode/Downloads/sparkle_your_name_am720p.mp4");
        // 分块之后的存储路径
        File chunkFolder = new File("/Users/swcode/Downloads/chunk/");
        File mergeFile = new File("/Users/swcode/Downloads/sparkle_your_name_am720p_merge.mp4");
        File[] files = chunkFolder.listFiles();
        if (files == null) {
            return;
        }
        List<File> sortFileList = Arrays.asList(files);
        sortFileList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));

        RandomAccessFile raf_rw = new RandomAccessFile(mergeFile, "rw");

        byte[] bytes = new byte[1024];
        for (File file : sortFileList) {
            RandomAccessFile raf_r = new RandomAccessFile(file, "r");
            int len = -1;
            while ((len = raf_r.read(bytes)) != -1) {
                raf_rw.write(bytes, 0, len);
            }
            raf_r.close();
        }
        raf_rw.close();

        // 合并完成检验
        String sourceMD5 = DigestUtils.md5DigestAsHex(new FileInputStream(sourceFile));
        String mergeMD5 = DigestUtils.md5DigestAsHex(new FileInputStream(mergeFile));
        if (sourceMD5.equals(mergeMD5)) {
            System.out.println("合并成功");
        }
    }
}
