package com.swx.orders.service;

import com.swx.orders.model.dto.AddOrderDTO;
import com.swx.orders.model.po.XcOrders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-04
 */
public interface XcOrdersService extends IService<XcOrders> {

    /**
     * 保存订单信息
     *
     * @param userId 用户ID
     * @param dto    订单信息
     */
    XcOrders saveXcOrders(String userId, AddOrderDTO dto);
}
