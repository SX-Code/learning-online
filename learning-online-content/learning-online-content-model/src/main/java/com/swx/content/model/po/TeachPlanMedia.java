package com.swx.content.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("teachplan_media")
public class TeachPlanMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 媒资文件id
     */
    @TableField("media_id")
    private String mediaId;

    /**
     * 课程计划标识
     */
    @TableField("teachplan_id")
    private Long teachplanId;

    /**
     * 课程标识
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 媒资文件原始名称
     */
    @TableField("media_fileName")
    private String mediaFilename;

    @TableField("create_date")
    private Date createDate;

    /**
     * 创建人
     */
    @TableField("create_people")
    private String createPeople;

    /**
     * 修改人
     */
    @TableField("change_people")
    private String changePeople;


}
