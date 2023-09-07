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
@TableName("xc_course_tables")
public class XcCourseTables implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 选课记录id
     */
    @TableField("choose_course_id")
    private Long chooseCourseId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 课程id
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 机构id
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 课程名称
     */
    @TableField("course_name")
    private String courseName;

    /**
     * 课程类型
     */
    @TableField("course_type")
    private String courseType;

    /**
     * 添加时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 开始服务时间
     */
    @TableField("validtime_start")
    private LocalDateTime validtimeStart;

    /**
     * 到期时间
     */
    @TableField("validtime_end")
    private LocalDateTime validtimeEnd;

    /**
     * 更新时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;


}
