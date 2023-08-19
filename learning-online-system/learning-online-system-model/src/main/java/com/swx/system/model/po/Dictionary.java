package com.swx.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dictionary")
public class Dictionary {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;

    /**
     * 数据字典名称
     */
    @TableField("name")
    public String name;

    /**
     * 数据字典代码
     */
    @TableField("code")
    public String code;

    /**
     * 数据字典项--json格式
     */
    @TableField("item_values")
    public String itemValues;
}
