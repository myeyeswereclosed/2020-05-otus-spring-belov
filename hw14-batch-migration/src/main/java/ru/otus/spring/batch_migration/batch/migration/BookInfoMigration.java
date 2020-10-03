package ru.otus.spring.batch_migration.batch.migration;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.spring.batch_migration.batch.migration.author.AuthorMigration;
import ru.otus.spring.batch_migration.batch.migration.book.BookMigration;
import ru.otus.spring.batch_migration.batch.migration.document.CommentDocument;
import ru.otus.spring.batch_migration.batch.migration.genre.GenreMigration;
import ru.otus.spring.batch_migration.config.MigrationConfig;

@Component
@Configuration
@RequiredArgsConstructor
public class BookInfoMigration {
    private static final Logger logger = LoggerFactory.getLogger(BookInfoMigration.class);

    private final JobBuilderFactory jobBuilderFactory;

    private final AuthorMigration authorMigration;
    private final GenreMigration genreMigration;
    private final BookMigration bookMigration;
    private final ClearingStep clearingStep;

    private final MigrationConfig migrationConfig;

    @Bean
    public Job migrationJob() {
        var commonBuilder =
            buildJob()
                .next(authorMigration.migrate())
                .next(genreMigration.migrate())
                .next(bookMigration.migrate())
            ;

        var jobBuilder =
            migrationConfig.isClearSourceId()
                ? commonBuilder.next(authorMigration.clearSourceId())
                : commonBuilder;

        return
            jobBuilder
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info(name() + " started");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info(name() + " finished");
                    }
                })
                .build()
            ;
    }

    public String name() {
        return migrationConfig.getJobName();
    }

    private SimpleJobBuilder buildJob() {
        return
            jobBuilderFactory
                .get(migrationConfig.getJobName())
                .incrementer(new RunIdIncrementer())
                .start(authorMigration.clear())
                .next(genreMigration.clear())
                .next(bookMigration.clear())
                .next(clearingStep.run(CommentDocument.class))
            ;
    }
}
