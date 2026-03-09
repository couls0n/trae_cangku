package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("audit_log")
public class AuditLog extends BaseEntity {

    private String userId;

    private String username;

    private String operationType;

    private String operationModule;

    private String operationContent;

    private String ipAddress;

    private String userAgent;

    private String result;

    private String remark;
}
