package com.swx.media.service;

import com.swx.media.model.po.MediaProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-25
 */
public interface MediaProcessService extends IService<MediaProcess> {
    /**
     * 获取待处理任务
     *
     * @param shardTotal 分片总数
     * @param shardIndex 当前分片序号
     * @param count      任务数量
     * @return List<MediaProcess>
     */
    List<MediaProcess> getMediaProcessList(int shardTotal, int shardIndex, int count);

    /**
     * 开启一个任务
     *
     * @param id 任务id
     * @return true 开启任务成功，false开启任务失败
     */
    public boolean startTask(long id);

}
