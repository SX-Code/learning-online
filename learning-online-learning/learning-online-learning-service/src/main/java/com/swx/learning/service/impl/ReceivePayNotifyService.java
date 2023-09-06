package com.swx.learning.service.impl;

import com.alibaba.fastjson2.JSON;
import com.swx.base.exception.BizException;
import com.swx.learning.config.PayNotifyConfig;
import com.swx.learning.service.MyCourseTablesService;
import com.swx.messagesdk.model.po.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReceivePayNotifyService {

    private final MyCourseTablesService myCourseTablesService;

    public ReceivePayNotifyService(MyCourseTablesService myCourseTablesService) {
        this.myCourseTablesService = myCourseTablesService;
    }

    @RabbitListener(queues = PayNotifyConfig.PAY_NOTIFY_QUEUE)
    public void receive(Message message) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.debug("消费睡眠异常:", e);
        }

        // 解析消息，转为对象
        String jsonMessage = new String(message.getBody());
        MqMessage mqMessage = JSON.parseObject(jsonMessage, MqMessage.class);
        // 订单类型
        String orderType = mqMessage.getBusinessKey2();
        if (!orderType.equals("60201")) {
            // 不是购买课程的订单类型
            return;
        }
        String chooseCourseId = mqMessage.getBusinessKey1();
        // 更新选课记录表
        boolean b = myCourseTablesService.payChooseCourseSuccess(chooseCourseId);
        if (b) {
            throw new BizException("保存选课记录状态失败");
        }
    }
}
