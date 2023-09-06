package com.swx.orders.service;

import com.swx.orders.model.po.XcOrders;
import com.swx.orders.model.po.XcPayRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-04
 */
public interface XcPayRecordService extends IService<XcPayRecord> {

    /**
     * 创建支付记录
     *
     * @param orders 订单
     */
    XcPayRecord createPayRecord(XcOrders orders);

    /**
     * 根据交易编号查询记录
     *
     * @param payNo 交易编号
     * @return com.swx.orders.model.po.XcPayRecord 支付记录
     */
    XcPayRecord getOneByPayNo(String payNo);
}
