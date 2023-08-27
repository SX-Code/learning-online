package com.swx.media.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author sw-code
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("media_process")
public class MediaProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件标识
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 文件名称
     */
    @TableField("filename")
    private String filename;

    /**
     * 存储桶
     */
    @TableField("bucket")
    private String bucket;

    /**
     * 存储路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 状态,1:未处理，2：处理成功  3处理失败
     */
    @TableField("status")
    private String status;

    /**
     * 上传时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 完成时间
     */
    @TableField("finish_date")
    private LocalDateTime finishDate;

    /**
     * 失败次数
     */
    @TableField("fail_count")
    private Integer failCount;

    /**
     * 媒资文件访问地址
     */
    @TableField("url")
    private String url;

    /**
     * 失败原因
     */
    @TableField("errormsg")
    private String errormsg;


}
