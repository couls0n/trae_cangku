package com.warehouse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 操作审计日志队列
    public static final String AUDIT_LOG_QUEUE = "audit_log_queue";
    // 操作审计日志交换机
    public static final String AUDIT_LOG_EXCHANGE = "audit_log_exchange";
    // 操作审计日志路由键
    public static final String AUDIT_LOG_ROUTING_KEY = "audit.log";

    @Bean
    public Queue auditLogQueue() {
        return new Queue(AUDIT_LOG_QUEUE, true);
    }

    @Bean
    public DirectExchange auditLogExchange() {
        return new DirectExchange(AUDIT_LOG_EXCHANGE, true, false);
    }

    @Bean
    public Binding auditLogBinding() {
        return BindingBuilder.bind(auditLogQueue())
                .to(auditLogExchange())
                .with(AUDIT_LOG_ROUTING_KEY);
    }
}
