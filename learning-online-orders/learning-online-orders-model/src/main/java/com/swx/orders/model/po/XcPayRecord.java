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
@TableName("xc_pay_record")
public class XcPayRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 本系统支付交易号
     */
    @TableField("pay_no")
    private Long payNo;

    /**
     * 第三方支付交易流水号
     */
    @TableField("out_pay_no")
    private String outPayNo;

    /**
     * 第三方支付渠道编号
     */
    @TableField("out_pay_channel")
    private String outPayChannel;

    /**
     * 商品订单号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 订单名称
     */
    @TableField("order_name")
    private String orderName;

    /**
     * 订单总价单位元
     */
    @TableField("total_price")
    private Float totalPrice;

    /**
     * 币种CNY
     */
    @TableField("currency")
    private String currency;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 支付状态
     */
    @TableField("status")
    private String status;

    /**
     * 支付成功时间
     */
    @TableField("pay_success_time")
    private LocalDateTime paySuccessTime;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;


}
