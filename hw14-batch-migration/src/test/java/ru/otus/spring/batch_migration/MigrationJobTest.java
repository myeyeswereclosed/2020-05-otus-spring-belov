package ru.otus.spring.batch_migration;

import lombok.*;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import ru.otus.spring.batch_migration.batch.migration.WithName;
import ru.otus.spring.batch_migration.batch.migration.document.AuthorDocument;
import ru.otus.spring.batch_migration.batch.migration.document.BookDocument;
import ru.otus.spring.batch_migration.batch.migration.document.CommentDocument;
import ru.otus.spring.batch_migration.batch.migration.document.GenreDocument;
import ru.otus.spring.batch_migration.batch.migration.model.*;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class MigrationJobTest {
    private static final AuthorModel PUSHKIN = new AuthorModel(1, "Alexander", "Pushkin");
    private static final AuthorModel LERMONTOV = new AuthorModel(2, "Michail", "Lermontov");
    private static final AuthorModel UNKNOWN_AUTHOR = new AuthorModel(3, "Unknown", "Author");

    private static final GenreModel HORROR = new GenreModel(1, "horror");
    private static final GenreModel UNUSED_GENRE = new GenreModel(2, "unused");
    private static final GenreModel ANIMALS = new GenreModel(3, "animals");
    private static final GenreModel FAIRY_TAIL = new GenreModel(4, "fairy-tail");

    private static final CommentModel ABOUT_CATS = new CommentModel(2, "O kotah");
    private static final CommentModel GOOD_COMMENT = new CommentModel(2, "Good book!");
    private static final CommentModel SUPER_COMMENT = new CommentModel(4, "Super book!");

    private static final List<AuthorModel> ALL_AUTHORS = List.of(PUSHKIN, LERMONTOV, UNKNOWN_AUTHOR);
    private static final List<GenreModel> ALL_GENRES = List.of(HORROR, UNUSED_GENRE, ANIMALS, FAIRY_TAIL);
    private static final List<CommentModel> ALL_COMMENTS = List.of(ABOUT_CATS, GOOD_COMMENT, SUPER_COMMENT);

    private static final List<BookInfo> BOOKS_TO_MIGRATE =
        List.of(
            new BookInfo(
                1,
                "1 v pole ne voin",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
            ),
            new BookInfo(
                2,
                "2 kota",
                List.of(PUSHKIN, LERMONTOV),
                List.of(HORROR),
                List.of(ABOUT_CATS, GOOD_COMMENT)
            ),
            new BookInfo(
                3,
                "3 porosenka",
                List.of(LERMONTOV),
                List.of(HORROR, ANIMALS),
                Collections.emptyList()
            ),
            new BookInfo(
                4,
                "4 skazki",
                List.of(PUSHKIN),
                List.of(FAIRY_TAIL),
                List.of(SUPER_COMMENT)
            ),
            new BookInfo(
                5,
                "5 ozer",
                List.of(LERMONTOV, PUSHKIN),
                List.of(HORROR, ANIMALS, FAIRY_TAIL),
                Collections.emptyList()
            )
        );

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private JobRepositoryTestUtils jobRepository;

    @Autowired
    private JdbcOperations jdbc;

    @Autowired
    private MongoTemplate mongo;

    @Test
    public void testJob() throws Exception {
        assertPreconditions();

        jobLauncher.launchJob();

        assertMigrationResult();
    }

    private void assertPreconditions() {
        assertStoredBooks();

        assertStoredAuthors();
        assertBookAuthors();

        assertStoredGenres();
        assertBookGenres();

        assertStoredComments();
    }

    private void assertStoredBooks() {
        var books = jdbc.query(
            "select id, title from book",
            new BeanPropertyRowMapper<>(BookModel.class)
        );

        assertResults(books, bookInfo -> new BookModel(bookInfo.getId(), bookInfo.getTitle()));
    }

    private void assertStoredAuthors() {
        var authors = jdbc.query(
            "select id, first_name, last_name from author",
            new BeanPropertyRowMapper<>(AuthorModel.class)
        );

        assertThat(authors).isEqualTo(ALL_AUTHORS);
    }

    private void assertStoredGenres() {
        var authors = jdbc.query(
            "select id, name from genre",
            new BeanPropertyRowMapper<>(GenreModel.class)
        );

        assertThat(authors).isEqualTo(ALL_GENRES);
    }

    private void assertBookAuthors() {
        var authorIdsPerBookId = authorIdsPerBookId();

        assertThat(authorIdsPerBookId)
            .allSatisfy(
                (bookId, bookIdAuthorId) ->
                    assertThat(bookInfoById(bookId))
                        .get()
                        .satisfies(
                            bookInfo ->
                                assertResults(
                                    bookInfo.getAuthors(),
                                    authorIdsPerBookId.get(bookId),
                                    AuthorModel::getId,
                                    BookAuthor::getAuthorId
                                )
                        )
            );
    }

    private void assertBookGenres() {
        var genreIdsPerBookId = genreIdsPerBookId();

        assertThat(genreIdsPerBookId)
            .allSatisfy(
                (bookId, bookIdGenreId) ->
                    assertThat(bookInfoById(bookId))
                        .get()
                        .satisfies(
                            bookInfo ->
                                assertResults(
                                    bookInfo.getGenres(),
                                    genreIdsPerBookId.get(bookId),
                                    GenreModel::getId,
                                    BookGenre::getGenreId                                )
                        )
            );
    }

    private void assertStoredComments() {
        var storedComments = jdbc.query(
            "select book_id, text from comment",
            new BeanPropertyRowMapper<>(CommentModel.class)
        );

        assertThat(storedComments).isEqualTo(ALL_COMMENTS);

        var commentsPerBookId = commentsPerBookId(storedComments);

        assertThat(commentsPerBookId)
            .allSatisfy(
                (bookId, comments) ->
                    assertThat(bookInfoById(bookId))
                        .get()
                        .satisfies(
                            bookInfo ->
                                assertResults(
                                    bookInfo.getComments(),
                                    commentsPerBookId.get(bookId),
                                    CommentModel::getText,
                                    CommentModel::getText                                )
                        )
            );
    }

    private Optional<BookInfo> bookInfoById(long bookId) {
        return
            BOOKS_TO_MIGRATE
                .stream()
                .filter(bookInfo -> bookInfo.getId() == bookId)
                .findFirst();
    }

    private Map<Long, List<BookAuthor>> authorIdsPerBookId() {
        var bookAuthors =
            jdbc.query(
                "select book_id, author_id from book_author",
                new BeanPropertyRowMapper<>(BookAuthor.class)
            );

        return bookAuthors.stream().collect(groupingBy(BookAuthor::getBookId));
    }

    private Map<Long, List<BookGenre>> genreIdsPerBookId() {
        var bookGenres =
            jdbc.query(
                "select book_id, genre_id from book_genre",
                new BeanPropertyRowMapper<>(BookGenre.class)
            );

        return bookGenres.stream().collect(groupingBy(BookGenre::getBookId));
    }

    private Map<Long, List<CommentModel>> commentsPerBookId(List<CommentModel> comments) {
        return comments.stream().collect(groupingBy(CommentModel::getBookId));
    }

    private void assertMigrationResult() {
        assertMigratedAuthors();
        assertMigratedGenres();

        var migratedBooks = mongo.findAll(BookDocument.class);

        assertMigratedBooks(migratedBooks);
        assertMigratedComments(migratedBookIdsToItsComments(migratedBooks));
    }

    private Map<String, List<CommentModel>> migratedBookIdsToItsComments(List<BookDocument> migratedBooks) {
        return
            migratedBooks
                .stream()
                .map(
                    migrated ->
                        new BookDocumentIdToSourceComments(
                            migrated.getId(),
                            BOOKS_TO_MIGRATE
                                .stream()
                                .filter(bookInfo -> bookInfo.getTitle().equals(migrated.getTitle()))
                                .flatMap(bookInfo -> bookInfo.getComments().stream())
                                .collect(toList())
                        )
                )
                .collect(groupingBy(BookDocumentIdToSourceComments::getId))
                .entrySet()
                .stream()
                .collect(
                    toMap(
                        Map.Entry::getKey,
                        entry -> {
                            assertThat(entry.getValue()).hasSize(1);
                            return entry.getValue().get(0).getComments();
                        }
                    )
                );
    }

    private void assertMigratedAuthors() {
        var migratedAuthors = mongo.findAll(AuthorDocument.class);

        assertThat(migratedAuthors)
            .allSatisfy(
                authorDocument -> {
                    assertThat(authorDocument.getId()).isNotNull();
                    assertThat(authorDocument.getId()).isNotBlank();
                });
        assertAuthorMigratedWithoutDuplicates(migratedAuthors);
    }

    private void assertMigratedGenres() {
        var migratedGenres = mongo.findAll(GenreDocument.class);

        assertThat(migratedGenres)
            .allSatisfy(
                genre -> {
                    assertThat(genre.getId()).isNotNull();
                    assertThat(genre.getId()).isNotBlank();
                });
        assertGenreMigratedWithoutDuplicates(migratedGenres);
    }

    private void assertMigratedBooks(List<BookDocument> booksMigrated) {
        assertResults(booksMigrated, BookDocument::getTitle, BookInfo::getTitle);

        assertResults(
            booksMigrated,
            bookMigrated -> names(bookMigrated.getAuthors()),
            bookInfo -> names(bookInfo.getAuthors())
        );

        assertResults(
            booksMigrated,
            bookMigrated -> names(bookMigrated.getGenres()),
            bookInfo -> names(bookInfo.getGenres())
        );
    }

    private void assertMigratedComments(Map<String, List<CommentModel>> migratedBookIdToSourceBookInfo) {
        var migratedComments = mongo.findAll(CommentDocument.class);

        assertThat(migratedComments)
            .allSatisfy(
                comment -> {
                    assertThat(comment.getId()).isNotNull();
                    assertThat(comment.getId()).isNotBlank();
                });

        var migratedCommentsPerBookId =
            migratedComments
                .stream()
                .collect(groupingBy(comment -> comment.getBook().getId()));

        assertThat(migratedCommentsPerBookId)
            .allSatisfy(
                (bookId, commentDocuments) -> {
                    var sourceComments = migratedBookIdToSourceBookInfo.get(bookId);

                    assertResults(
                        commentDocuments,
                        sourceComments,
                        CommentDocument::getText,
                        CommentModel::getText
                    );
                }
            );
    }

    // actual and expected lists are mapped to the same type
    private<T, U, R> void assertResults(
        List<T> actual,
        List<U> expected,
        Function<T, R> actualToResultType,
        Function<U, R> expectedToResultType
    ) {
        // check sizes are equal
        assertThat(actual).hasSize(expected.size());
        // check mapped contents are the same
        assertThat(actual.stream().map(actualToResultType).collect(toSet()))
            .isEqualTo(expected.stream().map(expectedToResultType).collect(toSet()));
    }

    private<T, R> void assertResults(
        List<T> actual,
        Function<T, R> actualToResultType,
        Function<BookInfo, R> initialDataToResultType
    ) {
        assertResults(actual, BOOKS_TO_MIGRATE, actualToResultType, initialDataToResultType);
    }

    private<T> void assertResults(
        List<T> actual,
        Function<BookInfo, T> initialDataToResultType
    ) {
        assertResults(actual, t -> t, initialDataToResultType);
    }

    private void assertAuthorMigratedWithoutDuplicates(List<AuthorDocument> migratedAuthors) {
        assertThat(names(migratedAuthors)).isEqualTo(names(ALL_AUTHORS));
    }

    private void assertGenreMigratedWithoutDuplicates(List<GenreDocument> migratedGenres) {
        assertThat(names(migratedGenres)).isEqualTo(names(ALL_GENRES));
    }

    private Set<String> names(List<? extends WithName> authors) {
        return authors.stream().map(WithName::getName).collect(toSet());
    }

    @NoArgsConstructor
    @Data
    private static class BookAuthor {
        private long bookId;
        private long authorId;
    }

    @NoArgsConstructor
    @Data
    private static class BookGenre {
        private long bookId;
        private int genreId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class BookDocumentIdToSourceComments {
        private String id;
        private List<CommentModel> comments;
    }
}
