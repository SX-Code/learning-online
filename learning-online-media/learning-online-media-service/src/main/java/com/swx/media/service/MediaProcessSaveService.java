package com.swx.media.service;

public interface MediaProcessSaveService {

    /**
     * 保存任务结果
     *
     * @param taskId   任务ID
     * @param status   任务状态
     * @param fileId   文件id
     * @param url      转码路径
     * @param errorMsg 错误信息
     */
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
