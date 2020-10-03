package ru.otus.spring.batch_migration.batch.migration.genre;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.otus.spring.batch_migration.batch.migration.ClearingStep;
import ru.otus.spring.batch_migration.batch.migration.document.GenreDocument;
import ru.otus.spring.batch_migration.batch.migration.model.GenreModel;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreMigration {
    private static final Logger logger = LoggerFactory.getLogger(GenreMigration.class);

    private final StepBuilderFactory stepBuilderFactory;
    private final JdbcOperations jdbc;
    private final MongoTemplate mongoTemplate;

    private final ClearingStep clearingStep;

    public Step clear() {
        return clearingStep.run(GenreDocument.class);
    }

    public Step migrate() {
        return
            stepBuilderFactory
                .get("Migrate genres")
                .tasklet(
                    (contribution, chunkContext) -> {
                        var allGenres = jdbc.query(
                            "select name from genre",
                            new BeanPropertyRowMapper<>(GenreModel.class)
                        );

                        mongoTemplate
                            .insertAll(
                                allGenres
                                    .stream()
                                    .map(genre -> new GenreDocument(genre.getName()))
                                    .collect(Collectors.toList())
                            );

                        return RepeatStatus.FINISHED;
                    }
                )
                .listener(
                    new StepExecutionListener() {
                        @Override
                        public void beforeStep(StepExecution stepExecution) {
                            logger.info("Begin genres migration");
                        }

                        @Override
                        public ExitStatus afterStep(StepExecution stepExecution) {
                            logger.info("Genres successfully migrated");

                            return ExitStatus.COMPLETED;
                        }
                    }
                )
                .build();
    }
}
