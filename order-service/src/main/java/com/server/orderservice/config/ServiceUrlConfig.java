package com.server.orderservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom.config")
public class ServiceUrlConfig {

    private Service user;
    private Service product;
    private Service payment;

    @Data
    public static class Service {
        private String url;
    }
}
