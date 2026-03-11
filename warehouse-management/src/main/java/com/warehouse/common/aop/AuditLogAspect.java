package com.warehouse.common.aop;

import com.warehouse.common.amqp.AuditLogProducer;
import com.warehouse.entity.AuditLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditLogAspect {

    @Autowired(required = false)
    private AuditLogProducer auditLogProducer;

    @Pointcut("execution(* com.warehouse.controller.*.*(..))")
    public void controllerPointcut() {}

    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (auditLogProducer == null) {
            return;
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            AuditLog auditLog = new AuditLog();
            auditLog.setUserId("1");
            auditLog.setUsername("admin");
            auditLog.setOperationType(getOperationType(joinPoint.getSignature().getName()));
            auditLog.setOperationModule(joinPoint.getTarget().getClass().getSimpleName());
            auditLog.setOperationContent(joinPoint.getSignature().toShortString());
            auditLog.setIpAddress(request.getRemoteAddr());
            auditLog.setUserAgent(request.getHeader("User-Agent"));
            auditLog.setResult("SUCCESS");
            auditLog.setCreateTime(LocalDateTime.now());
            auditLog.setUpdateTime(LocalDateTime.now());

            auditLogProducer.sendAuditLog(auditLog);
        } catch (Exception e) {
            // 忽略审计日志错误，不影响主业务
        }
    }

    private String getOperationType(String methodName) {
        if (methodName.startsWith("get") || methodName.startsWith("list") || methodName.startsWith("page")) {
            return "QUERY";
        } else if (methodName.startsWith("save") || methodName.startsWith("add")) {
            return "CREATE";
        } else if (methodName.startsWith("update")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete")) {
            return "DELETE";
        } else {
            return "OTHER";
        }
    }
}
