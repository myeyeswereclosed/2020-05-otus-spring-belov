package ru.otus.spring.batch_migration.batch.migration.book;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.spring.batch_migration.batch.migration.document.AuthorDocument;
import ru.otus.spring.batch_migration.batch.migration.document.BookDocument;
import ru.otus.spring.batch_migration.batch.migration.document.CommentDocument;
import ru.otus.spring.batch_migration.batch.migration.document.GenreDocument;
import ru.otus.spring.batch_migration.batch.migration.model.AuthorModel;
import ru.otus.spring.batch_migration.batch.migration.model.BookInfo;
import ru.otus.spring.batch_migration.batch.migration.model.GenreModel;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class BookInfoWriter implements ItemWriter<BookInfo> {
    private static final Logger logger = LoggerFactory.getLogger(BookInfoWriter.class);

    private final MongoTemplate mongoTemplate;

    @Override
    public void write(List<? extends BookInfo> bookInfoList) throws Exception {
        logger.info("Start writing process for {} books", bookInfoList.size());

        var migratedAuthors = findMigratedAuthors(bookInfoList);
        var migratedGenres= findMigratedGenres(bookInfoList);

        var comments =
            bookInfoList
                .stream()
                .flatMap(bookInfo -> handleBookInfo(bookInfo, migratedAuthors, migratedGenres).stream())
                .collect(toList());

        mongoTemplate.insertAll(comments);
    }

    private List<CommentDocument> handleBookInfo(
        BookInfo bookInfo,
        List<AuthorDocument> migratedAuthors,
        List<GenreDocument> migratedGenres
    ) {
        var bookDocument =
            new BookDocument(
                bookInfo.getTitle(),
                bookAuthors(migratedAuthors, bookInfo),
                bookGenres(migratedGenres, bookInfo)
            );

        var storedBookDocument = mongoTemplate.insert(bookDocument);

        return
            bookInfo
                .getComments()
                .parallelStream()
                .map(comment -> new CommentDocument(comment.getText(), storedBookDocument))
                .collect(toList());
    }

    private List<AuthorDocument> bookAuthors(List<AuthorDocument> migrated, BookInfo bookInfo) {
        return
            migrated
                .stream()
                .filter(
                    document ->
                        bookInfo
                            .getAuthors()
                            .stream()
                            .map(AuthorModel::getId)
                            .collect(toList())
                            .contains(document.getSourceId())
                )
                .collect(toList());
    }

    private List<GenreDocument> bookGenres(List<GenreDocument> alreadyMigrated, BookInfo bookInfo) {
        return
            alreadyMigrated
                .stream()
                .filter(
                    document ->
                        bookInfo
                            .getGenres()
                            .stream()
                            .map(GenreModel::getName)
                            .collect(toList())
                            .contains(document.getName())
                )
                .collect(toList());
    }

    private List<AuthorDocument> findMigratedAuthors(List<? extends BookInfo> bookInfoList) {
        var authorIds =
            bookInfoList
                .stream()
                .flatMap(bookInfo -> bookInfo.getAuthors().stream().map(AuthorModel::getId))
                .distinct()
                .collect(toList())
            ;

        return mongoTemplate.find(new Query(Criteria.where("sourceId").in(authorIds)), AuthorDocument.class);
    }

    private List<GenreDocument> findMigratedGenres(List<? extends BookInfo> bookInfoList) {
        var genreNames =
            bookInfoList
                .stream()
                .flatMap(bookInfo -> bookInfo.getGenres().stream().map(GenreModel::getName))
                .distinct()
                .collect(toList())
        ;

        return mongoTemplate.find(new Query(Criteria.where("name").in(genreNames)), GenreDocument.class);
    }
}
