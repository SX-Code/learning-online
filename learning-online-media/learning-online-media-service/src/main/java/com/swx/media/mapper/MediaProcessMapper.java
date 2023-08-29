package com.swx.media.mapper;

import com.swx.media.model.po.MediaProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-08-25
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {

    /**
     * 查询当前分片的任务
     *
     * @param shardTotal 分片总数
     * @param shardIndex 当前分片序号
     * @param count      任务数量
     * @return List<MediaProcess>
     */
    @Select("select * from media_process t where t.id % #{shardTotal} = #{shardIndex} and t.status <> 2 and t.fail_count < 3 limit #{count}")
    List<MediaProcess> selectListByShardIndex(@Param("shardTotal") int shardTotal, @Param("shardIndex") int shardIndex, @Param("count") int count);

    /**
     * 开启一个任务
     * @param id 任务id
     * @return int 更新成功条数
     */
    @Update("update media_process set status = '4' where (status='1' or status='3') and fail_count<3 and id=#{id}")
    int startTask(@Param("id") Long id);
}
