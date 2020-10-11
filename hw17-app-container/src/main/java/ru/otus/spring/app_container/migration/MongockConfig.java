package ru.otus.spring.app_container.migration;

import com.github.cloudyrock.mongock.SpringMongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.app_container.config.DatabaseConfig;
import ru.otus.spring.app_container.config.MigrationConfig;

@Configuration
public class MongockConfig {
    @Bean
    public SpringMongock mongock(
        MongoClient mongoClient,
        DatabaseConfig databaseConfig,
        MigrationConfig migrationConfig
    ) {
        return
            new SpringMongockBuilder(
                mongoClient,
                databaseConfig.getDatabase(),
                migrationConfig.getChangelogPackage()
            )
                .build();
    }
}
