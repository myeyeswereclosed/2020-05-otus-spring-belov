package ru.otus.spring.batch_migration.batch.migration.book;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ru.otus.spring.batch_migration.batch.migration.ClearingStep;
import ru.otus.spring.batch_migration.batch.migration.document.BookDocument;
import ru.otus.spring.batch_migration.batch.migration.model.BookInfo;
import ru.otus.spring.batch_migration.batch.migration.model.BookModel;
import ru.otus.spring.batch_migration.config.MigrationConfig;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMigration {
    private static final Logger logger = LoggerFactory.getLogger(BookMigration.class);

    private final StepBuilderFactory stepBuilderFactory;
    private final MigrationConfig migrationConfig;

    private final ItemReader<BookModel> reader;
    private final ItemProcessor<BookModel, BookInfo> processor;
    private final ItemWriter<BookInfo> writer;
    private final ClearingStep clearingStep;

    public Step clear() {
        return clearingStep.run(BookDocument.class);
    }

    public Step migrate() {
        return
            stepBuilderFactory
                .get(migrationConfig.getStepName())
                .<BookModel, BookInfo>chunk(migrationConfig.getChunkSize())
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Begin reading...");
                    }

                    @Override
                    public void afterRead(BookModel book) {
                        logger.info("Successfully read {}", book.toString());
                    }

                    public void onReadError(Exception e) {
                        logger.error("Reading error {}", e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<BookInfo>() {
                    public void beforeWrite(List list) {
                        logger.info("Writing {} bookInfo items", list.size());
                    }

                    public void afterWrite(List list) {
                        logger.info("Successfully written {} bookInfo items as collections", list.size());
                    }

                    public void onWriteError(Exception e, List list) {
                        logger.error("Writing error {} occurred with items: {}", e.getMessage(), list.toString());
                    }
                })
                .listener(new ItemProcessListener<BookModel, BookInfo>() {
                    public void beforeProcess(BookModel book) {
                        logger.info("Start processing book {}", book.getId());
                    }

                    @Override
                    public void afterProcess(BookModel book, BookInfo bookInfo) {
                        logger.info("Processed: {} -> {}", book.getId(), bookInfo.toString());
                    }

                    @Override
                    public void onProcessError(BookModel book, Exception e) {
                        logger.error("Exception {} occurred for {}", e.getMessage(), book.toString());
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(ChunkContext chunkContext) {
                        logger.info("Starting chunk of {} books", migrationConfig.getChunkSize());
                    }

                    public void afterChunk(ChunkContext chunkContext) {
                        logger.info("Finished chunk of {} books", migrationConfig.getChunkSize());
                    }

                    public void afterChunkError(ChunkContext chunkContext) {
                        logger.error("Chunk error occurred");
                    }
                })
                .build()
            ;
    }

}
