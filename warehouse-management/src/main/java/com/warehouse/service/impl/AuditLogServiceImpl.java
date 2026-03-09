package com.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.entity.AuditLog;
import com.warehouse.mapper.AuditLogMapper;
import com.warehouse.service.AuditLogService;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {
}
