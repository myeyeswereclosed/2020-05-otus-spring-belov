package ru.otus.spring.batch_migration.batch.migration.author;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.otus.spring.batch_migration.batch.migration.ClearingStep;
import ru.otus.spring.batch_migration.batch.migration.document.AuthorDocument;
import ru.otus.spring.batch_migration.batch.migration.model.AuthorModel;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthorMigration {
    private static final Logger logger = LoggerFactory.getLogger(AuthorMigration.class);

    private final StepBuilderFactory stepBuilderFactory;
    private final JdbcOperations jdbc;
    private final MongoTemplate mongo;

    private final ClearingStep clearingStep;

    public Step clear() {
        return clearingStep.run(AuthorDocument.class);
    }

    public Step migrate() {
        return
            stepBuilderFactory
                .get("Migrate authors")
                .tasklet(
                    (contribution, chunkContext) -> {
                        var allAuthors = jdbc.query(
                            "select id, first_name, last_name from author",
                            new BeanPropertyRowMapper<>(AuthorModel.class)
                        );

                        mongo
                            .insertAll(
                                allAuthors
                                    .stream()
                                    .map(author ->
                                        new AuthorDocument(
                                            author.getId(),
                                            author.getFirstName(),
                                            author.getLastName())
                                    )
                                    .collect(Collectors.toList())
                            );

                        return RepeatStatus.FINISHED;
                    }
                )
                .listener(
                    new StepExecutionListener() {
                        @Override
                        public void beforeStep(StepExecution stepExecution) {
                            logger.info("Begin authors migration");
                        }

                        @Override
                        public ExitStatus afterStep(StepExecution stepExecution) {
                            logger.info("Authors successfully migrated");

                            return ExitStatus.COMPLETED;
                        }
                    }
                )
                .build()
            ;
    }

    public Step clearSourceId() {
        return
            stepBuilderFactory
                .get("Clear source id")
                .tasklet(
                    (contribution, chunkContext) -> {
                        mongo.updateMulti(new Query(), new Update().unset("sourceId"), AuthorDocument.class);

                        return RepeatStatus.FINISHED;
                    }
                )
                .listener(
                    new StepExecutionListener() {
                        @Override
                        public void beforeStep(StepExecution stepExecution) {
                            logger.info("Begin clearing author sourceId");
                        }

                        @Override
                        public ExitStatus afterStep(StepExecution stepExecution) {
                            logger.info("Successfully cleared authors sourceId");

                            return ExitStatus.COMPLETED;
                        }
                    }
                )
                .build();
    }
}
