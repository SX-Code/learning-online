package com.swx.orders.model.po;

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
 * @since 2023-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("xc_orders_goods")
public class XcOrdersGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 商品id
     */
    @TableField("goods_id")
    private String goodsId;

    /**
     * 商品类型
     */
    @TableField("goods_type")
    private String goodsType;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 商品交易价，单位分
     */
    @TableField("goods_price")
    private Float goodsPrice;

    /**
     * 商品详情json
     */
    @TableField("goods_detail")
    private String goodsDetail;


}
