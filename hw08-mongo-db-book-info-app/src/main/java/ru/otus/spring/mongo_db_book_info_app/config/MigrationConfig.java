package ru.otus.spring.mongo_db_book_info_app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("migration")
public class MigrationConfig {
    private String changelogPackage;
}
