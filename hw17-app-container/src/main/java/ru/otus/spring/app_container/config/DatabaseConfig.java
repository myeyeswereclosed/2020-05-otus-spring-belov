package ru.otus.spring.app_container.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.data.mongodb")
public class DatabaseConfig {
    private int port;
    private String database;
}
