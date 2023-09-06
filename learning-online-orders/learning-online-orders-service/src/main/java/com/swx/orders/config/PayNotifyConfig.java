package com.swx.orders.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.swx.messagesdk.model.po.MqMessage;
import com.swx.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PayNotifyConfig implements ApplicationContextAware {

    // 交换机
    public static final String PAY_NOTIFY_EXCHANGE_FANOUT = "pay_notify_exchange_fanout";
    // 支付结果通知消息类型
    public static final String Message_TYPE = "pay_result_notify";
    // 支付通知队列
    public static final String PAY_NOTIFY_QUEUE = "pay_notify_queue";

    // 声明交换机，且持久化
    @Bean(PAY_NOTIFY_EXCHANGE_FANOUT)
    public FanoutExchange payNotifyExchangeFanout() {
        // 三个参数：交换机名称、是否持久化、当没有queue与其绑定时是否自动删除
        return new FanoutExchange(PAY_NOTIFY_EXCHANGE_FANOUT, true, false);
    }

    // 支付通知队列
    @Bean(PAY_NOTIFY_QUEUE)
    public Queue coursePublishQueue() {
        return QueueBuilder.durable(PAY_NOTIFY_QUEUE).build();
    }

    // 交换机和支付队列绑定
    @Bean
    public Binding bindingCoursePublishQueue(@Qualifier(PAY_NOTIFY_QUEUE) Queue queue, @Qualifier(PAY_NOTIFY_EXCHANGE_FANOUT) FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * 当消息投递失败时，将消息写入到本地消息表
     * exchange到queue失败
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 消息处理service
        MqMessageService mqMessageService = applicationContext.getBean(MqMessageService.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            // 交换机到队列投递失败，记录日志
            log.info("消息发送失败, 应答码: {}, 原因: {}, 交换机: {}, 路由键: {}, 消息: {}",
                    replyCode, replyText, exchange, routingKey, message);
            MqMessage mqMessage = JSON.parseObject(message.toString(), MqMessage.class);
            // 将消息添加到消息表
            mqMessageService.addMessage(mqMessage.getMessageType(), mqMessage.getBusinessKey1(), mqMessage.getBusinessKey2(), mqMessage.getBusinessKey3());
        }));
    }
}
