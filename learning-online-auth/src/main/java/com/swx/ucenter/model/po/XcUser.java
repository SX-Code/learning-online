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
@TableName("xc_user")
public class XcUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    /**
     * 微信unionid
     */
    @TableField("wx_unionid")
    private String wxUnionid;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    @TableField("name")
    private String name;

    /**
     * 头像
     */
    @TableField("userpic")
    private String userpic;

    @TableField("company_id")
    private String companyId;

    @TableField("utype")
    private String utype;

    @TableField("birthday")
    private LocalDateTime birthday;

    @TableField("sex")
    private String sex;

    @TableField("email")
    private String email;

    @TableField("cellphone")
    private String cellphone;

    @TableField("qq")
    private String qq;

    /**
     * 用户状态
     */
    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


}
