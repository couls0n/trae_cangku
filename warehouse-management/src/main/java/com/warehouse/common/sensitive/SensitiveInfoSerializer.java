package com.warehouse.common.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class SensitiveInfoSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private SensitiveType sensitiveType;

    public SensitiveInfoSerializer() {
    }

    public SensitiveInfoSerializer(SensitiveType sensitiveType) {
        this.sensitiveType = sensitiveType;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null && sensitiveType != null) {
            String maskedValue = SensitiveUtil.mask(value.toString(), sensitiveType);
            gen.writeString(maskedValue);
        } else {
            serializers.defaultSerializeValue(value, gen);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, com.fasterxml.jackson.databind.BeanProperty property) {
        if (property != null) {
            SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
            if (sensitiveInfo != null) {
                return new SensitiveInfoSerializer(sensitiveInfo.type());
            }
        }
        return this;
    }
}
