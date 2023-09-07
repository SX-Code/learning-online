package com.swx.orders.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.swx.base.exception.BizException;
import com.swx.base.utils.QRCodeUtil;
import com.swx.messagesdk.model.po.MqMessage;
import com.swx.messagesdk.service.MqMessageService;
import com.swx.orders.config.AlipayConfig;
import com.swx.orders.config.PayNotifyConfig;
import com.swx.orders.model.dto.AddOrderDTO;
import com.swx.orders.model.dto.PayStatusDTO;
import com.swx.orders.model.po.XcOrders;
import com.swx.orders.model.po.XcPayRecord;
import com.swx.orders.model.vo.PayRecordVO;
import com.swx.orders.service.OrderService;
import com.swx.orders.service.XcOrdersService;
import com.swx.orders.service.XcPayRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${pay.alipay.APP_ID}")
    private String APP_ID;
    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;
    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;

    private final XcOrdersService xcOrdersService;
    private final XcPayRecordService xcPayRecordService;
    private final TransactionTemplate transactionTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final MqMessageService mqMessageService;

    @Value("${pay.qrcodeUrl}")
    private String qrcodeUrl;

    public OrderServiceImpl(XcOrdersService xcOrdersService, XcPayRecordService xcPayRecordService,
                            TransactionTemplate transactionTemplate, RabbitTemplate rabbitTemplate,
                            MqMessageService mqMessageService) {
        this.xcOrdersService = xcOrdersService;
        this.xcPayRecordService = xcPayRecordService;
        this.transactionTemplate = transactionTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.mqMessageService = mqMessageService;
    }

    /**
     * 创建订单
     * 新增订单信息，新增支付记录，生成支付二维码
     *
     * @param userId 用户id
     * @param dto    订单信息
     * @return 二维码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayRecordVO createOrder(String userId, AddOrderDTO dto) {
        // 新增订单信息
        XcOrders orders = xcOrdersService.saveXcOrders(userId, dto);

        if (orders.getStatus().equals("601002")) {
            // 已支付
            throw new BizException("订单已支付");
        }
        // 新增支付记录
        XcPayRecord payRecord = xcPayRecordService.createPayRecord(orders);

        String url = String.format(qrcodeUrl, payRecord.getPayNo());
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        // 二维码图片
        String qrCode = null;
        try {
            qrCode = qrCodeUtil.createQRCode(url, 200, 200);
        } catch (IOException e) {
            throw new BizException("生成二维码出错");
        }

        PayRecordVO payRecordVO = new PayRecordVO();
        BeanUtils.copyProperties(payRecord, payRecordVO);
        payRecordVO.setQrcode(qrCode);
        return payRecordVO;
    }

    /**
     * 查询支付结果
     *
     * @param payNo 支付交易号
     * @return 交付结果
     */
    @Override
    public PayRecordVO queryRequestPay(String payNo) {
        PayStatusDTO payStatusDTO = queryPayResultFromAlipay(payNo);
        // 更新
        transactionTemplate.executeWithoutResult(status -> {
            try {
                saveAlipayStatus(payStatusDTO);
            } catch (Exception e) {
                status.setRollbackOnly();
            }
        });
        XcPayRecord oneByPayNo = xcPayRecordService.getOneByPayNo(payNo);
        PayRecordVO payRecordVO = new PayRecordVO();
        BeanUtils.copyProperties(oneByPayNo, payRecordVO);
        return payRecordVO;
    }

    /**
     * 更新支付记录和订单状态
     * @param dto 支付宝支付状态
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAlipayStatus(PayStatusDTO dto) {
        String payNo = dto.getOut_trade_no();
        XcPayRecord oneByPayNo = xcPayRecordService.getOneByPayNo(payNo);
        if (oneByPayNo == null) {
            throw new BizException("找不到相关的支付记录");
        }
        Long orderId = oneByPayNo.getOrderId();
        XcOrders xcOrder = xcOrdersService.getById(orderId);
        if (xcOrder == null) {
            throw new BizException("找不到相关连的订单");
        }
        String status = oneByPayNo.getStatus();
        if (status.equals("601002")) {
            return;
        }
        String tradeStatus = dto.getTrade_status();
        if (!tradeStatus.equals("TRADE_SUCCESS")) {
            return;
        }
        oneByPayNo.setStatus("601002");
        // 支付宝订单号
        oneByPayNo.setOutPayNo(dto.getTrade_no());
        oneByPayNo.setOutPayChannel("Alipay");
        oneByPayNo.setPaySuccessTime(LocalDateTime.now());
        xcPayRecordService.updateById(oneByPayNo);

        // 更新订单表状态
        xcOrder.setStatus("600002");
        xcOrdersService.updateById(xcOrder);

        // 消息写入数据库, 消息类型，选课ID，购买课程（订单类型），null
        MqMessage mqMessage = mqMessageService.addMessage("pay_result_notify", xcOrder.getOutBusinessId(), xcOrder.getOrderType(), null);
        // 发送消息
        notifyPayResult(mqMessage);
    }

    /**
     * 通知支付结果
     *
     * @param mqMessage 结果
     */
    @Override
    public void notifyPayResult(MqMessage mqMessage) {
        // 1. 将消息体转为Json
        String jsonMsg = JSON.toJSONString(mqMessage);
        // 2. 设消息的持久化方式为PERSISTENT，即消息会被持久化到磁盘上，确保即使在RabbitMQ服务器重启后也能够恢复消息。
        Message msgObj = MessageBuilder.withBody(jsonMsg.getBytes()).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        // 3. 封装CorrelationData，用于跟踪消息的相关信息
        CorrelationData correlationData = new CorrelationData(mqMessage.getId().toString());
        // 3.1 添加一个Callback对象，该对象用于在消息确认时处理消息的结果
        correlationData.getFuture().addCallback(result -> {
            if (result != null && result.isAck()) {
                // 3.2 消息成功发送到交换机，删除消息表中的记录
                log.debug("消息发送成功：{}", jsonMsg);
                mqMessageService.completed(mqMessage.getId());
            } else {
                // 3.3 消息发送失败
                log.error("消息发送失败，id：{}，原因：{}", mqMessage.getId(), result);
            }
        }, ex -> {
            // 3.4 消息异常
            log.error("消息发送异常，id：{}，原因：{}", mqMessage.getId(), ex.getMessage());
        });
        // 4. 发送消息
        // 发送消息
        rabbitTemplate.convertAndSend(PayNotifyConfig.PAY_NOTIFY_EXCHANGE_FANOUT, "", msgObj , correlationData);
    }

    /**
     * 查询支付宝，获取支付状态
     *
     * @param payNo 支付交易号
     * @return 交付结果
     */
    public PayStatusDTO queryPayResultFromAlipay(String payNo) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new BizException("请求支付查询支付结果异常");
        }
        if (!response.isSuccess()) {
            throw new BizException("请求支付查询支付结果失败");
        }

        String body = response.getBody();
        // 解析支付结果
        Map bodyMap = JSON.parseObject(body, Map.class);
        Map alipayTradeQueryResponse = (Map) bodyMap.get("alipay_trade_query_response");

        String tradeNo = (String) alipayTradeQueryResponse.get("trade_no");
        String tradeStatus = (String) alipayTradeQueryResponse.get("trade_status");
        String totalAmount = (String) alipayTradeQueryResponse.get("total_amount");
        PayStatusDTO payStatusDTO = new PayStatusDTO();
        payStatusDTO.setOut_trade_no(payNo);
        payStatusDTO.setTrade_no(tradeNo);
        payStatusDTO.setTrade_status(tradeStatus);
        payStatusDTO.setApp_id(APP_ID);
        payStatusDTO.setTotal_amount(totalAmount);
        return payStatusDTO;
    }
}
