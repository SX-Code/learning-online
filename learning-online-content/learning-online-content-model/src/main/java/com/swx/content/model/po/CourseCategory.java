package com.swx.content.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课程分类
 * </p>
 *
 * @author sw-code
 * @since 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_category")
public class CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 分类标签默认和名称一样
     */
    @TableField("label")
    private String label;

    /**
     * 父结点id（第一级的父节点是0，自关联字段id）
     */
    @TableField("parentid")
    private String parentid;

    /**
     * 是否显示
     */
    @TableField("is_show")
    private Integer isShow;

    /**
     * 排序字段
     */
    @TableField("orderby")
    private Integer orderby;

    /**
     * 是否叶子
     */
    @TableField("is_leaf")
    private Integer isLeaf;


}
