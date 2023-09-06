package com.swx.learning.config;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消费端配置一份，保证消费端先启动时不会报错
 */
@Configuration
public class PayNotifyConfig {

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
}
