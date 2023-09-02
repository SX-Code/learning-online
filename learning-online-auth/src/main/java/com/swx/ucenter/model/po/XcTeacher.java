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
@TableName("xc_teacher")
public class XcTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 称呼
     */
    @TableField("name")
    private String name;

    /**
     * 个人简介
     */
    @TableField("intro")
    private String intro;

    /**
     * 个人简历
     */
    @TableField("resume")
    private String resume;

    /**
     * 老师照片
     */
    @TableField("pic")
    private String pic;


}
