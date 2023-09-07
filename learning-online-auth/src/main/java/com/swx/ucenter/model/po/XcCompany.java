package com.swx.ucenter.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xc_company")
public class XcCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 联系人名称
     */
    @TableField("linkname")
    private String linkname;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    @TableField("mobile")
    private String mobile;

    @TableField("email")
    private String email;

    /**
     * 简介
     */
    @TableField("intro")
    private String intro;

    /**
     * logo
     */
    @TableField("logo")
    private String logo;

    /**
     * 身份证照片
     */
    @TableField("identitypic")
    private String identitypic;

    /**
     * 工具性质
     */
    @TableField("worktype")
    private String worktype;

    /**
     * 营业执照
     */
    @TableField("businesspic")
    private String businesspic;

    /**
     * 企业状态
     */
    @TableField("status")
    private String status;


}
