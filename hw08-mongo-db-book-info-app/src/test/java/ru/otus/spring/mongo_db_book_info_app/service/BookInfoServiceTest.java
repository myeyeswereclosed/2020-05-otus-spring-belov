package ru.otus.spring.mongo_db_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.repository.AuthorRepository;
import ru.otus.spring.mongo_db_book_info_app.repository.GenreRepository;
import ru.otus.spring.mongo_db_book_info_app.service.book.BookInfoService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DisplayName("Сервис сводной информации о книгах должен ")
@SpringBootTest
public class BookInfoServiceTest {
    private final static String INITIAL_BOOK_NOT_FOUND_MESSAGE = "Initial book not found";

    private static final String INITIAL_BOOK_TITLE = "Tri porosenka";
    private static final String INITIAL_COMMENT_TEXT = "Good book!";

    private static final Author INITIAL_AUTHOR = new Author("Some", "Author");
    private static final Author NEW_AUTHOR = new Author("Oleg", "Kotov");

    private static final String INITIAL_GENRE_NAME = "horror";
    private static final String NEW_GENRE_NAME = "love-story";

    private static final String NEW_COMMENT_TEXT = "Super book!";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookInfoService service;

    @DisplayName("находить книгу по идентификатору")
    @Test
    public void get() {
        findInitial()
            .ifPresentOrElse(
                this::assertInitialBookInfo,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private Optional<BookInfo> findInitial() {
        return service.getAll().value().map(bookInfoList -> bookInfoList.get(0));
    }

    @DisplayName("отдавать пустой результат, если не книга не найдена")
    @Test
    public void emptyIfNotFound() {
        var result = service.get("");

        assertThat(result.isOk());
        assertThat(result.value().isEmpty()).isTrue();
    }

    @DisplayName("успешно добавлять нового автора к хранимой книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addNewBookAuthor() {
        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    assertThat(authorRepository.findAll().size()).isEqualTo(1);
                    testNewBookAuthorAdded(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testNewBookAuthorAdded(Book initialBook) {
        var result = service.addBookAuthor(initialBook.getId(), NEW_AUTHOR);

        assertThat(authorRepository.findAll().size()).isEqualTo(2);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(Book::getAuthors).extracting(List::size).isEqualTo(2);

        assertThat(result.value())
            .get()
            .extracting(Book::getAuthors).extracting(authors -> authors.get(1))
            .satisfies(
                author ->
                    assertThat(author.hasFirstAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName()))
                        .isTrue()
            );

        assertThat(result.value())
            .get()
            .satisfies(
                bookFound -> {
                    assertThat(bookFound.getId()).isEqualTo(initialBook.getId());
                    assertThat(bookFound.getTitle()).isEqualTo(initialBook.getTitle());
                }
            );
    }

    @DisplayName("игнорировать добавление уже существующего автора книги")
    @Test
    public void addExistingBookAuthor() {
        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    testExistingAuthorIgnored(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testExistingAuthorIgnored(Book initialBook) {
        var result = service.addBookAuthor(initialBook.getId(), INITIAL_AUTHOR);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).get().satisfies(this::assertInitialBook);
    }

    @DisplayName("успешно добавлять сохраненного автора как автора книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addStoredAuthorAsBookAuthor() {
        var author = new Author(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName());

        authorRepository.save(author);

        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    assertThat(authorRepository.findAll().size()).isEqualTo(2);
                    testNewBookAuthorAdded(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    @DisplayName("успешно добавлять новый жанр к хранимой книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addNewBookGenre() {
        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    assertThat(genreRepository.findAll().size()).isEqualTo(1);
                    testNewBookGenreAdded(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testNewBookGenreAdded(Book initialBook) {
        var result = service.addBookGenre(initialBook.getId(), new Genre(NEW_GENRE_NAME));

        assertThat(genreRepository.findAll().size()).isEqualTo(2);

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(Book::getGenres).extracting(List::size).isEqualTo(2);

        assertThat(result.value())
            .get()
            .extracting(Book::getGenres).extracting(genres -> genres.get(1))
            .satisfies(genre -> assertThat(genre.hasName(NEW_GENRE_NAME)).isTrue())
        ;

        assertThat(result.value())
            .get()
            .satisfies(
                bookFound -> {
                    assertThat(bookFound.getId()).isEqualTo(initialBook.getId());
                    assertThat(bookFound.getTitle()).isEqualTo(initialBook.getTitle());
                }
            );
    }

    @DisplayName("игнорировать добавление уже существующего жанра книги")
    @Test
    public void addExistingBookGenre() {
        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    testExistingGenreIgnored(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testExistingGenreIgnored(Book initialBook) {
        var result = service.addBookGenre(initialBook.getId(), new Genre(INITIAL_GENRE_NAME));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).get().satisfies(this::assertInitialBook);
    }

    @DisplayName("успешно добавлять сохраненный жанр как жанр книги")
    @Test
    public void addStoredGenreAsBookGenre() {
        var genre = new Genre(NEW_GENRE_NAME);

        genreRepository.save(genre);

        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    assertThat(genreRepository.findAll().size()).isEqualTo(2);
                    testNewBookGenreAdded(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    @DisplayName("добавлять комментарий к книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addComment() {
        findInitial()
            .ifPresentOrElse(
                bookInfo -> {
                    assertInitialBookInfo(bookInfo);
                    testCommentAdded(bookInfo.getBook());
                },
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testCommentAdded(Book book) {
        var result = service.addComment(book.getId(), new Comment(NEW_COMMENT_TEXT));

        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).get().extracting(Comment::getId).isNotNull();
        assertThat(result.value()).get().extracting(Comment::getText).isEqualTo(NEW_COMMENT_TEXT);
        assertThat(result.value()).get().extracting(Comment::getBook).isEqualTo(book);

        assertThat(service.get(book.getId()).value())
            .get()
            .extracting(BookInfo::getComments)
            .extracting(List::size)
            .isEqualTo(2)
        ;
    }

    @DisplayName("находить все книги с информацией по ним")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void getAll() {
        var result = service.getAll();

        assertThat(result.isOk()).isTrue();

        assertThat(result.value()).get().extracting(List::size).isEqualTo(1);
        assertThat(result.value())
            .get()
            .satisfies(
                bookInfoList -> {
                    assertThat(bookInfoList.size()).isEqualTo(1);
                    assertInitialBookInfo(bookInfoList.get(0));
                }
            );
    }

    private void assertInitialBookInfo(BookInfo bookInfo) {
        assertInitialBook(bookInfo.getBook());

        assertThat(bookInfo.getComments().size()).isEqualTo(1);
        assertThat(bookInfo.getComments().get(0).getText()).isEqualTo(INITIAL_COMMENT_TEXT);
    }

    private void assertInitialBook(Book book) {
        assertThat(book.getTitle()).isEqualTo(INITIAL_BOOK_TITLE);

        assertThat(book.getAuthors().size()).isEqualTo(1);
        assertThat(book.getAuthors().get(0)).satisfies(
            author -> assertThat(
                author.hasFirstAndLastName(
                    INITIAL_AUTHOR.getFirstName(),
                    INITIAL_AUTHOR.getLastName()
                )
            )
        );

        assertThat(book.getGenres().size()).isEqualTo(1);
        assertThat(book.getGenres().get(0))
            .satisfies(genre -> assertThat(genre.hasName(NEW_GENRE_NAME)));
    }
}
