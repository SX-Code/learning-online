package com.swx.ucenter.model.po;

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
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xc_menu")
public class XcMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 菜单编码
     */
    @TableField("code")
    private String code;

    /**
     * 父菜单ID
     */
    @TableField("p_id")
    private String pId;

    /**
     * 名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 请求地址
     */
    @TableField("url")
    private String url;

    /**
     * 是否是菜单
     */
    @TableField("is_menu")
    private String isMenu;

    /**
     * 菜单层级
     */
    @TableField("level")
    private Integer level;

    /**
     * 菜单排序
     */
    @TableField("sort")
    private Integer sort;

    @TableField("status")
    private String status;

    @TableField("icon")
    private String icon;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
