package ru.otus.spring.batch_migration.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import ru.otus.spring.batch_migration.batch.migration.model.BookModel;

import javax.sql.DataSource;

@EnableBatchProcessing
@RequiredArgsConstructor
@Configuration
public class JobConfig {
    private final MigrationConfig migrationConfig;
    private final DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<BookModel> bookReader(PagingQueryProvider queryProvider) {
        return
            new JdbcPagingItemReaderBuilder<BookModel>()
                .name("bookReader")
                .dataSource(dataSource)
                .pageSize(migrationConfig.getPageSize())
                .queryProvider(queryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(BookModel.class))
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider() {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();

        provider.setDataSource(dataSource);
        provider.setSelectClause("select id, title");
        provider.setFromClause("from book");
        provider.setSortKey("id");

        return provider;
    }

    public String name() {
        return migrationConfig.getJobName();
    }
}
