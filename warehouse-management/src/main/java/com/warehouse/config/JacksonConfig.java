package com.warehouse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.warehouse.common.sensitive.SensitiveInfo;
import com.warehouse.common.sensitive.SensitiveInfoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .build();
        
        // 注册自定义序列化器模块
        SimpleModule module = new SimpleModule();
        module.addSerializer(Object.class, new SensitiveInfoSerializer());
        mapper.registerModule(module);
        
        return new MappingJackson2HttpMessageConverter(mapper);
    }
}
