package ru.otus.spring.batch_migration.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="migration")
@Data
@NoArgsConstructor
public class MigrationConfig {
    private int pageSize;
    private int chunkSize;
    private String jobName;
    private String stepName;
    private boolean clearSourceId;
}
