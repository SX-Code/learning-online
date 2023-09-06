package com.swx.learning.model.po;

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
 * @since 2023-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xc_learn_record")
public class XcLearnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 课程名称
     */
    @TableField("course_name")
    private String courseName;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 最近学习时间
     */
    @TableField("learn_date")
    private LocalDateTime learnDate;

    /**
     * 学习时长
     */
    @TableField("learn_length")
    private Long learnLength;

    /**
     * 章节id
     */
    @TableField("teachplan_id")
    private Long teachplanId;

    /**
     * 章节名称
     */
    @TableField("teachplan_name")
    private String teachplanName;


}
