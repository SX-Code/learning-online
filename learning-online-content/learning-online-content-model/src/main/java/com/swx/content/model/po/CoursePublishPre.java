package com.swx.content.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课程发布
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_publish_pre")
public class CoursePublishPre implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 机构ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 课程名称
     */
    @TableField("name")
    private String name;

    /**
     * 适用人群
     */
    @TableField("users")
    private String users;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 创建人
     */
    @TableField("username")
    private String username;

    /**
     * 大分类
     */
    @TableField("mt")
    private String mt;

    /**
     * 大分类名称
     */
    @TableField("mt_name")
    private String mtName;

    /**
     * 小分类
     */
    @TableField("st")
    private String st;

    /**
     * 小分类名称
     */
    @TableField("st_name")
    private String stName;

    /**
     * 课程等级
     */
    @TableField("grade")
    private String grade;

    /**
     * 教育模式
     */
    @TableField("teachmode")
    private String teachmode;

    /**
     * 课程图片
     */
    @TableField("pic")
    private String pic;

    /**
     * 课程介绍
     */
    @TableField("description")
    private String description;

    /**
     * 课程营销信息，json格式
     */
    @TableField("market")
    private String market;

    /**
     * 所有课程计划，json格式
     */
    @TableField("teachplan")
    private String teachplan;

    /**
     * 教师信息，json格式
     */
    @TableField("teachers")
    private String teachers;

    /**
     * 提交时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 审核时间
     */
    @TableField("audit_date")
    private LocalDateTime auditDate;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 收费规则，对应数据字典--203
     */
    @TableField("charge")
    private String charge;

    /**
     * 现价
     */
    @TableField("price")
    private Float price;

    /**
     * 原价
     */
    @TableField("original_price")
    private Float originalPrice;

    /**
     * 课程有效期天数
     */
    @TableField("valid_days")
    private Integer validDays;


}
