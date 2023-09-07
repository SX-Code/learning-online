package com.swx.orders.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 创建商品订单
 */
@Data
@ToString
public class AddOrderDTO {

    /**
     * 总价
     */
    @NotNull(message = "价格不能为空")
    private Float totalPrice;

    /**
     * 订单类型
     */
    @NotEmpty(message = "未知订单类型")
    private String orderType;

    /**
     * 订单名称
     */
    private String orderName;
    /**
     * 订单描述
     */
    @NotEmpty(message = "请添加订单描述")
    private String orderDescrip;

    /**
     * 订单明细json，不可为空
     * [{"goodsId":"","goodsType":"","goodsName":"","goodsPrice":"","goodsDetail":""},{...}]
     */
    @NotEmpty(message = "订单明细不可为空")
    private String orderDetail;

    /**
     * 外部系统业务id
     */
    @NotEmpty(message = "请先选课")
    private String outBusinessId;

}
