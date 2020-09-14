package ru.otus.spring.app_authorization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("migration")
public class MigrationConfig {
    private String changelogPackage;
}
