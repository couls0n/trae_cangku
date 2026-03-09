package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.sensitive.SensitiveInfo;
import com.warehouse.common.sensitive.SensitiveType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {

    private String customerCode;

    private String customerName;

    @SensitiveInfo(type = SensitiveType.NAME)
    private String contactPerson;

    @SensitiveInfo(type = SensitiveType.PHONE)
    private String phone;

    @SensitiveInfo(type = SensitiveType.ADDRESS)
    private String address;

    @SensitiveInfo(type = SensitiveType.EMAIL)
    private String email;

    private Integer status;

    private String remark;
}
