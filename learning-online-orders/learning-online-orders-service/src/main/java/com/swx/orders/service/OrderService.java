package com.swx.orders.service;

import com.swx.messagesdk.model.po.MqMessage;
import com.swx.orders.model.dto.AddOrderDTO;
import com.swx.orders.model.dto.PayStatusDTO;
import com.swx.orders.model.vo.PayRecordVO;

/**
 * 订单相关Service
 */
public interface OrderService {

    /**
     * 创建订单
     * 新增订单信息，新增支付记录，生成支付二维码
     *
     * @param userId 用户id
     * @param dto    订单信息
     * @return 二维码
     */
    public PayRecordVO createOrder(String userId, AddOrderDTO dto);

    /**
     * 查询支付结果
     *
     * @param payNo 支付交易号
     * @return 交付结果
     */
    PayRecordVO queryRequestPay(String payNo);

    /**
     * 更新支付记录和订单状态
     *
     * @param dto 支付宝支付状态
     */
    void saveAlipayStatus(PayStatusDTO dto);

    /**
     * 通知支付结果
     *
     * @param mqMessage 结果
     */
    public void notifyPayResult(MqMessage mqMessage);
}
