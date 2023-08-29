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
 * 媒资信息
 * </p>
 *
 * @author sw-code
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("media_files")
public class MediaFiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id,md5值
     */
    @TableId(value = "id")
    private String id;

    /**
     * 机构ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 机构名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 文件名称
     */
    @TableField("filename")
    private String filename;

    /**
     * 文件类型（图片、文档，视频）
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 存储目录
     */
    @TableField("bucket")
    private String bucket;

    /**
     * 存储路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件id
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 媒资文件访问地址
     */
    @TableField("url")
    private String url;

    /**
     * 上传人
     */
    @TableField("username")
    private String username;

    /**
     * 上传时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("change_date")
    private LocalDateTime changeDate;

    /**
     * 状态,1:正常，0:不展示
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 审核状态
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 审核意见
     */
    @TableField("audit_mind")
    private String auditMind;

    /**
     * 文件大小
     */
    @TableField("file_size")
    private Long fileSize;


}
