package com.warehouse.common.sensitive;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveInfo {
    SensitiveType type();
}
