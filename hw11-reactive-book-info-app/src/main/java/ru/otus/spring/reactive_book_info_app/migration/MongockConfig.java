package ru.otus.spring.reactive_book_info_app.migration;

import com.github.cloudyrock.mongock.SpringMongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.reactive_book_info_app.config.DatabaseConfig;
import ru.otus.spring.reactive_book_info_app.config.MigrationConfig;

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
