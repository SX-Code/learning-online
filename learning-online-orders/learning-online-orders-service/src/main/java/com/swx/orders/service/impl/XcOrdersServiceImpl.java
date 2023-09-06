package com.swx.orders.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.base.utils.IdWorkerUtil;
import com.swx.orders.model.dto.AddOrderDTO;
import com.swx.orders.model.po.XcOrders;
import com.swx.orders.mapper.XcOrdersMapper;
import com.swx.orders.model.po.XcOrdersGoods;
import com.swx.orders.service.XcOrdersGoodsService;
import com.swx.orders.service.XcOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-04
 */
@Service
public class XcOrdersServiceImpl extends ServiceImpl<XcOrdersMapper, XcOrders> implements XcOrdersService {

    private final XcOrdersGoodsService xcOrdersGoodsService;

    public XcOrdersServiceImpl(XcOrdersGoodsService xcOrdersGoodsService) {
        this.xcOrdersGoodsService = xcOrdersGoodsService;
    }

    /**
     * 保存订单信息
     *
     * @param userId 用户ID
     * @param dto    订单信息
     */
    @Override
    public XcOrders saveXcOrders(String userId, AddOrderDTO dto) {
        // 进行幂等性判断，同一个选课记录只能有一个订单
        XcOrders order = getOneByBizId(userId, dto.getOutBusinessId());
        if (order != null) {
            return order;
        }

        // 插入订单表
        order = new XcOrders();
        // 使用雪花算法生成订单号
        BeanUtils.copyProperties(dto, order);
        order.setId(IdWorkerUtil.getInstance().nextId());
        order.setCreateDate(LocalDateTime.now());
        order.setStatus("600001"); // 未支付
        order.setUserId(userId);
        order.setOrderType("60201"); // 业务订单类型，购买课程
        boolean save = save(order);
        if (!save) {
            throw new BizException("添加订单失败");
        }

        Long orderId = order.getId();
        String orderDetailJson = dto.getOrderDetail();
        List<XcOrdersGoods> xcOrdersGoods = JSON.parseArray(orderDetailJson, XcOrdersGoods.class);
        for (XcOrdersGoods xcOrdersGood : xcOrdersGoods) {
            xcOrdersGood.setOrderId(orderId);
        }
        boolean saveGoods = xcOrdersGoodsService.saveBatch(xcOrdersGoods);
        return order;
    }

    /**
     * 根据业务ID查询一条记录
     *
     * @param userId 用户ID
     * @param bizId  业务ID
     * @return com.swx.orders.model.po.XcOrders 订单
     */
    public XcOrders getOneByBizId(String userId, String bizId) {
        return getOne(Wrappers.<XcOrders>lambdaQuery().eq(XcOrders::getUserId, userId).eq(XcOrders::getOutBusinessId, bizId));
    }
}
