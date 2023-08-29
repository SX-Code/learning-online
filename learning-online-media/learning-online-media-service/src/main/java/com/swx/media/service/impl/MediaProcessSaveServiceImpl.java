package com.swx.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.media.model.enums.ProcessStatus;
import com.swx.media.model.po.MediaFiles;
import com.swx.media.model.po.MediaProcess;
import com.swx.media.model.po.MediaProcessHistory;
import com.swx.media.service.MediaFilesService;
import com.swx.media.service.MediaProcessHistoryService;
import com.swx.media.service.MediaProcessSaveService;
import com.swx.media.service.MediaProcessService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MediaProcessSaveServiceImpl implements MediaProcessSaveService {

    private final MediaFilesService mediaFilesService;
    private final MediaProcessService mediaProcessService;
    private final MediaProcessHistoryService mediaProcessHistoryService;

    public MediaProcessSaveServiceImpl(MediaFilesService mediaFilesService, MediaProcessService mediaProcessService,
                                       MediaProcessHistoryService mediaProcessHistoryService) {
        this.mediaFilesService = mediaFilesService;
        this.mediaProcessService = mediaProcessService;
        this.mediaProcessHistoryService = mediaProcessHistoryService;
    }

    /**
     * 保存任务结果
     *
     * @param taskId   任务ID
     * @param status   任务状态
     * @param fileId   文件id
     * @param url      转码路径
     * @param errorMsg 错误信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        MediaProcess dbProcess = mediaProcessService.getById(taskId);
        if (dbProcess == null) {
            return;
        }
        // 任务执行失败
        if (status.equals(ProcessStatus.PROCESS_FAIL.status())) {
            // 更新失败次数
            mediaProcessService.update(Wrappers.<MediaProcess>lambdaUpdate()
                    .eq(MediaProcess::getId, taskId)
                    .set(MediaProcess::getStatus, status)
                    .set(MediaProcess::getFailCount, dbProcess.getFailCount() + 1)
                    .set(MediaProcess::getErrormsg, errorMsg));
            return;
        }
        // 任务执行成功
        MediaFiles mediaFiles = mediaFilesService.getById(fileId);
        if (mediaFiles == null) {
            return;
        }

        // 更新media_file中的url
        mediaFilesService.update(Wrappers.<MediaFiles>lambdaUpdate().eq(MediaFiles::getId, fileId).set(MediaFiles::getUrl, url));
        // 将MediaProcess记录插入到MediaProcessHistory中
        dbProcess.setStatus(status);
        dbProcess.setFinishDate(LocalDateTime.now());
        dbProcess.setUrl(url);
        MediaProcessHistory processHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(dbProcess, processHistory);
        mediaProcessHistoryService.save(processHistory);

        // 删除MediaProcess记录
        mediaFilesService.removeById(taskId);
    }
}
