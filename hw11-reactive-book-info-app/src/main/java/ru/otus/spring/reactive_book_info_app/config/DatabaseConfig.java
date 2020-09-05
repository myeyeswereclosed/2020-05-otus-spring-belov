package ru.otus.spring.reactive_book_info_app.config;

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
