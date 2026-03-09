package com.warehouse.common.amqp;

import com.warehouse.entity.AuditLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.warehouse.config.RabbitMQConfig.*;

@Component
public class AuditLogProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAuditLog(AuditLog auditLog) {
        rabbitTemplate.convertAndSend(AUDIT_LOG_EXCHANGE, AUDIT_LOG_ROUTING_KEY, auditLog);
    }
}
