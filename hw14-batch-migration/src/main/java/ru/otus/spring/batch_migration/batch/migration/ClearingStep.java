package ru.otus.spring.batch_migration.batch.migration;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClearingStep {
    private static final Logger logger = LoggerFactory.getLogger(ClearingStep.class);

    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongo;

    public<T> Step run(Class<T> document) {
        var collection = document.getAnnotation(Document.class).collection();

        return
            stepBuilderFactory
                .get("Clear '" + collection + "' collection")
                .tasklet(clearing(mongo, document))
                .listener(
                    new StepExecutionListener() {
                        @Override
                        public void beforeStep(StepExecution stepExecution) {
                            logger.info("Begin clearing '{}' collection", collection);
                        }

                        @Override
                        public ExitStatus afterStep(StepExecution stepExecution) {
                            logger.info("Successfully cleared collection '{}'", collection);

                            return ExitStatus.COMPLETED;
                        }
                    }
                )
                .build();
    }

    private<T> Tasklet clearing(MongoTemplate template, Class<T> document) {
        return
            (contribution, chunkContext) -> {
                template.remove(document).all();

                return RepeatStatus.FINISHED;
            };
    }
}
