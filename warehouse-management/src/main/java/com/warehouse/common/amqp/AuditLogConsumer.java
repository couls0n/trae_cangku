package com.warehouse.common.amqp;

import com.warehouse.entity.AuditLog;
import com.warehouse.service.AuditLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.warehouse.config.RabbitMQConfig.AUDIT_LOG_QUEUE;

@Component
public class AuditLogConsumer {

    @Autowired
    private AuditLogService auditLogService;

    @RabbitListener(queues = AUDIT_LOG_QUEUE)
    public void handleAuditLog(AuditLog auditLog) {
        // 异步保存审计日志到数据库
        auditLogService.save(auditLog);
    }
}
