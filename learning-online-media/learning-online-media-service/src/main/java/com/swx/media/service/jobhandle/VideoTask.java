package com.swx.media.service.jobhandle;

import com.swx.media.model.enums.ProcessStatus;
import com.swx.media.model.po.MediaProcess;
import com.swx.media.service.FileStorageService;
import com.swx.media.service.MediaProcessSaveService;
import com.swx.media.service.MediaProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频转码任务处理类
 */
@Slf4j
@Component
public class VideoTask {

    private final MediaProcessService mediaProcessService;
    private final FileStorageService fileStorageService;
    private final MediaProcessSaveService mediaProcessSaveService;

    public VideoTask(MediaProcessService mediaProcessService, FileStorageService fileStorageService, MediaProcessSaveService mediaProcessSaveService) {
        this.mediaProcessService = mediaProcessService;
        this.fileStorageService = fileStorageService;
        this.mediaProcessSaveService = mediaProcessSaveService;
    }

    /**
     * 视频处理任务
     */
    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        // 确定CPU核心数
        int processors = Runtime.getRuntime().availableProcessors();
        // 拉取任务
        List<MediaProcess> mediaProcessList = mediaProcessService.getMediaProcessList(shardIndex, shardTotal, processors);
        int size = mediaProcessList.size();
        if (size == 0) {
            log.debug("取到的视频处理任务数：{}", size);
            return;
        }
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        // 使用计数器，等待所有线程执行完再结束方法
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (MediaProcess mediaProcess : mediaProcessList) {
            // 将任务加入线程池
            executorService.execute(() -> {
                try {

                    // 获取任务锁
                    Long taskId = mediaProcess.getId();
                    boolean lock = mediaProcessService.startTask(taskId);
                    String fileId = mediaProcess.getFileId();
                    if (!lock) {
                        log.debug("抢占任务失败，任务id: {}", taskId);
                        return;
                    }
                    // 下载MinIO视频
                    String bucket = mediaProcess.getBucket();
                    String objectName = mediaProcess.getFilePath();
                    File file = fileStorageService.downloadFile(bucket, objectName);
                    if (file == null) {
                        log.error("下载视频出错，任务id: {}, bucket: {}, objectName: {}", taskId, bucket, objectName);
                        // 保存任务处理失败结果
                        mediaProcessSaveService.saveProcessFinishStatus(taskId, ProcessStatus.PROCESS_FAIL.status(),
                                fileId, null, "下载视频到本地失败");
                        return;
                    }
                    // 视频转码
                    // 源avi视频的路径
                    File sourcePath = file.getAbsoluteFile();
                    File targetFile = null;
                    try {
                        targetFile = File.createTempFile("minio", ".mp4");
                    } catch (IOException e) {
                        log.error("创建临时文件异常, ", e);
                        // 保存任务处理失败结果
                        mediaProcessSaveService.saveProcessFinishStatus(taskId, ProcessStatus.PROCESS_FAIL.status(),
                                fileId, null, "创建临时文件异常");
                        return;
                    }
                    String targetPath = targetFile.getAbsolutePath();
                    // 开始转换
                    String result = "success";
                    if (!result.equals("success")) {
                        log.error("视频转码失败, bucket: {}, objectName: {},  原因: {}", taskId, bucket, result);
                        // 保存任务处理失败结果
                        mediaProcessSaveService.saveProcessFinishStatus(taskId, ProcessStatus.PROCESS_FAIL.status(),
                                fileId, null, result);
                        return;
                    }

                    // 上传到MinIO
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(targetPath);
                    } catch (FileNotFoundException e) {
                        log.error("转码后的文件不存在, 文件路径: {}", targetPath);
                        // 保存任务处理失败结果
                        mediaProcessSaveService.saveProcessFinishStatus(taskId, ProcessStatus.PROCESS_FAIL.status(),
                                fileId, null, "转码后的文件不存在");
                        return;
                    }
                    // 开始上传
                    boolean isUploadMinIO = fileStorageService.uploadVideoFile(objectName, "video/mp4", fileInputStream);
                    if (!isUploadMinIO) {
                        log.error("上传mp4到MinIO失败, taskId: {}", taskId);
                        // 保存任务处理失败结果
                        mediaProcessSaveService.saveProcessFinishStatus(taskId, ProcessStatus.PROCESS_FAIL.status(),
                                fileId, null, "上传mp4到MinIO失败");
                        return;
                    }
                    // 保存任务成功结果，mp4文件的url
                    String url = getFilePathByMd5(fileId, ".mp4");
                    mediaProcessSaveService.saveProcessFinishStatus(taskId, ProcessStatus.PROCESS_SUCCESS.status(),
                            fileId, url, null);
                } finally {
                    // 线程任务完成，计算器-1
                    countDownLatch.countDown();
                }
            });
        }

        // 阻塞，最多等待30分钟
        countDownLatch.await(30, TimeUnit.MINUTES);
    }

    /**
     * 得到MinIO文件的路径
     *
     * @param fileMd5 源文件Md5
     * @param suffix  文件后缀
     * @return 分块存储路径
     */
    private String getFilePathByMd5(String fileMd5, String suffix) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + suffix;
    }
}
