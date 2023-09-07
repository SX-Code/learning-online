package com.swx.orders.model.po;

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
 * @since 2023-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xc_orders")
public class XcOrders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 总价
     */
    @TableField("total_price")
    private Float totalPrice;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 交易状态
     */
    @TableField("status")
    private String status;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 订单类型
     */
    @TableField("order_type")
    private String orderType;

    /**
     * 订单名称
     */
    @TableField("order_name")
    private String orderName;

    /**
     * 订单描述
     */
    @TableField("order_descrip")
    private String orderDescrip;

    /**
     * 订单明细json
     */
    @TableField("order_detail")
    private String orderDetail;

    /**
     * 外部系统业务id
     */
    @TableField("out_business_id")
    private String outBusinessId;


}
