package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.sensitive.SensitiveInfo;
import com.warehouse.common.sensitive.SensitiveType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    private String username;

    private String password;

    @SensitiveInfo(type = SensitiveType.NAME)
    private String realName;

    @SensitiveInfo(type = SensitiveType.PHONE)
    private String phone;

    @SensitiveInfo(type = SensitiveType.EMAIL)
    private String email;

    private Integer status;

    private Integer role;
}
