package com.swx.orders.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.base.exception.BizException;
import com.swx.base.utils.IdWorkerUtil;
import com.swx.orders.model.po.XcOrders;
import com.swx.orders.model.po.XcPayRecord;
import com.swx.orders.mapper.XcPayRecordMapper;
import com.swx.orders.service.XcPayRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-09-04
 */
@Service
public class XcPayRecordServiceImpl extends ServiceImpl<XcPayRecordMapper, XcPayRecord> implements XcPayRecordService {

    /**
     * 创建支付记录
     *
     * @param orders 订单
     */
    @Override
    public XcPayRecord createPayRecord(XcOrders orders) {
        XcPayRecord xcPayRecord = new XcPayRecord();
        xcPayRecord.setPayNo(IdWorkerUtil.getInstance().nextId());
        xcPayRecord.setOrderId(orders.getId());
        xcPayRecord.setOrderName(orders.getOrderName());
        xcPayRecord.setTotalPrice(orders.getTotalPrice());
        xcPayRecord.setCurrency("CNY");
        xcPayRecord.setCreateDate(LocalDateTime.now());
        xcPayRecord.setStatus("601001"); // 未支付
        xcPayRecord.setUserId(orders.getUserId());
        boolean save = save(xcPayRecord);
        if (!save) {
            throw new BizException("添加支付记录失败");
        }
        return xcPayRecord;
    }

    /**
     * 根据交易编号查询记录
     *
     * @param payNo 交易编号
     * @return com.swx.orders.model.po.XcPayRecord 支付记录
     */
    @Override
    public XcPayRecord getOneByPayNo(String payNo) {
        return getOne(Wrappers.<XcPayRecord>lambdaQuery().eq(XcPayRecord::getPayNo, payNo));
    }
}
