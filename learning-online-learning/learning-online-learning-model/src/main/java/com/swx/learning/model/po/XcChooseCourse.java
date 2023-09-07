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
@TableName("xc_choose_course")
public class XcChooseCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
     * 机构id
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 选课类型
     */
    @TableField("order_type")
    private String orderType;

    /**
     * 添加时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 课程价格
     */
    @TableField("course_price")
    private Float coursePrice;

    /**
     * 课程有效期(天)
     */
    @TableField("valid_days")
    private Integer validDays;

    /**
     * 选课状态
     */
    @TableField("status")
    private String status;

    /**
     * 开始服务时间
     */
    @TableField("validtime_start")
    private LocalDateTime validtimeStart;

    /**
     * 结束服务时间
     */
    @TableField("validtime_end")
    private LocalDateTime validtimeEnd;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;


}
