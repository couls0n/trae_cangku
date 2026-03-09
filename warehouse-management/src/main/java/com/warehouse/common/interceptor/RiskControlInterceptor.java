package com.warehouse.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.warehouse.common.Result;
import com.warehouse.common.amqp.AuditLogProducer;
import com.warehouse.entity.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class RiskControlInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuditLogProducer auditLogProducer;

    private static final int MAX_REQUESTS_PER_MINUTE = 60; // 每分钟最大请求数
    private static final int MAX_REQUESTS_PER_SECOND = 5; // 每秒最大请求数

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        String requestUri = request.getRequestURI();
        String userId = "1";
        String username = "admin";

        // 检查API调用频率
        if (!checkRequestFrequency(clientIp, requestUri)) {
            logRiskEvent(request, userId, username, "API_FREQUENCY_LIMIT");
            sendErrorResponse(response, "API调用频率过高，请稍后再试");
            return false;
        }

        // 检查越权访问（示例：检查是否访问非本部门数据）
        if (checkUnauthorizedAccess(request, userId)) {
            logRiskEvent(request, userId, username, "UNAUTHORIZED_ACCESS");
            sendErrorResponse(response, "无权访问该资源");
            return false;
        }

        return true;
    }

    private boolean checkRequestFrequency(String clientIp, String requestUri) {
        // 每分钟请求限制
        String minuteKey = "rate_limit:minute:" + clientIp + ":" + requestUri;
        Long minuteCount = redisTemplate.opsForValue().increment(minuteKey);
        if (minuteCount == 1) {
            redisTemplate.expire(minuteKey, 1, TimeUnit.MINUTES);
        }
        if (minuteCount > MAX_REQUESTS_PER_MINUTE) {
            return false;
        }

        // 每秒请求限制
        String secondKey = "rate_limit:second:" + clientIp + ":" + requestUri;
        Long secondCount = redisTemplate.opsForValue().increment(secondKey);
        if (secondCount == 1) {
            redisTemplate.expire(secondKey, 1, TimeUnit.SECONDS);
        }
        return secondCount <= MAX_REQUESTS_PER_SECOND;
    }

    private boolean checkUnauthorizedAccess(HttpServletRequest request, String userId) {
        // 示例：检查是否访问非本部门的库存数据
        if (request.getRequestURI().contains("/api/stock") && request.getMethod().equals("GET")) {
            // 这里应该根据实际业务逻辑检查用户是否有权限访问该仓库的库存
            // 这里为了演示，假设用户只能访问仓库ID为1的数据
            String warehouseId = request.getParameter("warehouseId");
            if (warehouseId != null && !warehouseId.equals("1")) {
                return true; // 越权访问
            }
        }
        return false;
    }

    private void logRiskEvent(HttpServletRequest request, String userId, String username, String riskType) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setUsername(username);
        auditLog.setOperationType("RISK_CONTROL");
        auditLog.setOperationModule(request.getRequestURI());
        auditLog.setOperationContent(riskType);
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setUserAgent(request.getHeader("User-Agent"));
        auditLog.setResult("BLOCKED");
        auditLog.setRemark("风控拦截：" + riskType);
        auditLog.setCreateTime(LocalDateTime.now());
        auditLog.setUpdateTime(LocalDateTime.now());

        auditLogProducer.sendAuditLog(auditLog);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(Result.error(message)));
        out.flush();
        out.close();
    }
}
