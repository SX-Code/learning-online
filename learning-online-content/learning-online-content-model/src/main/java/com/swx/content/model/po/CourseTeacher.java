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
 * 课程-教师关系表
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_teacher")
public class CourseTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程标识
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 教师标识
     */
    @TableField("teacher_name")
    private String teacherName;

    /**
     * 教师职位
     */
    @TableField("position")
    private String position;

    /**
     * 教师简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 照片
     */
    @TableField("photograph")
    private String photograph;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;


}
