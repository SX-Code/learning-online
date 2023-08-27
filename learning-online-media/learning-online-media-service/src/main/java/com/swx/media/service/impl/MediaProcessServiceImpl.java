package com.swx.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.media.model.enums.ProcessStatus;
import com.swx.media.model.po.MediaProcess;
import com.swx.media.mapper.MediaProcessMapper;
import com.swx.media.service.MediaFilesService;
import com.swx.media.service.MediaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-25
 */
@Service
public class MediaProcessServiceImpl extends ServiceImpl<MediaProcessMapper, MediaProcess> implements MediaProcessService {

    /**
     * 获取待处理任务
     *
     * @param shardTotal 分片总数
     * @param shardIndex 当前分片序号
     * @param count      任务数量
     * @return List<MediaProcess>
     */
    @Override
    public List<MediaProcess> getMediaProcessList(int shardTotal, int shardIndex, int count) {
        return baseMapper.selectListByShardIndex(shardTotal, shardIndex, count);
    }

    /**
     * 开启一个任务
     *
     * @param id 任务id
     * @return true 开启任务成功，false开启任务失败
     */
    @Override
    public boolean startTask(long id) {
        return baseMapper.startTask(id) > 0;
    }
}
