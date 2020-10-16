package ru.otus.spring.hw18.comment_service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
public class DatabaseConfig {

    @Value("${spring.data.mongodb.uri}")
    private String test;

    private int port;
    private String database;
}
