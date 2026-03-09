package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.sensitive.SensitiveInfo;
import com.warehouse.common.sensitive.SensitiveType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse")
public class Warehouse extends BaseEntity {

    private String warehouseCode;

    private String warehouseName;

    @SensitiveInfo(type = SensitiveType.ADDRESS)
    private String address;

    @SensitiveInfo(type = SensitiveType.NAME)
    private String manager;

    @SensitiveInfo(type = SensitiveType.PHONE)
    private String phone;

    private Integer status;

    private String remark;
}
