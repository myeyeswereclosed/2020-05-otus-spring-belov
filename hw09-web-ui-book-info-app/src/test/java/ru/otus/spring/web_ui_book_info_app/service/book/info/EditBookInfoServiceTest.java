package ru.otus.spring.web_ui_book_info_app.service.book.info;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.dto.BookInfo;
import ru.otus.spring.web_ui_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.web_ui_book_info_app.repository.book.BookRepository;
import ru.otus.spring.web_ui_book_info_app.repository.genre.GenreRepository;
import ru.otus.spring.web_ui_book_info_app.service.book.info.edit.EditBookInfoService;
import ru.otus.spring.web_ui_book_info_app.service.book.info.get.GetBookInfoService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@DisplayName("Сервис изменения информации о книгах должен ")
@SpringBootTest
public class EditBookInfoServiceTest {
    private final static String INITIAL_BOOK_NOT_FOUND_MESSAGE = "Initial book not found";

    private static final String INITIAL_BOOK_TITLE = "Tri porosenka";
    private static final String INITIAL_COMMENT_TEXT = "Good book!";

    private static final Author INITIAL_AUTHOR = new Author("Some", "Author");
    private static final Author NEW_AUTHOR = new Author("Oleg", "Kotov");

    private static final String INITIAL_GENRE_NAME = "horror";
    private static final String NEW_GENRE_NAME = "love-story";

    private static final String NEW_BOOK_TITLE = "Tri kotenka";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GetBookInfoService getInfoService;

    @Autowired
    private EditBookInfoService service;

    private Optional<BookInfo> findInitial() {
        return getInfoService.getAll().value().map(bookInfoList -> bookInfoList.get(0));
    }

    @DisplayName("переименовать автора книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void renameAuthor() {
        findInitial()
            .ifPresentOrElse(
                this::testAuthorRenaming,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testAuthorRenaming(BookInfo initialBookInfo) {
        assertInitialBookInfo(initialBookInfo);

        var initialAuthor = initialBookInfo.getBook().getAuthors().get(0);

        var newBook = bookRepository.save(new Book(NEW_BOOK_TITLE).addAuthor(initialAuthor));

        assertThat(bookRepository.findAll().size()).isEqualTo(2);
        assertThat(newBook.getAuthors().get(0)).isEqualTo(initialAuthor);

        var result =
            service.renameAuthor(
                new Author(
                    initialAuthor.getId(),
                    NEW_AUTHOR.getFirstName(),
                    NEW_AUTHOR.getLastName()
                )
            );

        assertThat(result.isOk()).isTrue();

        result.value().ifPresentOrElse(
            author -> {
                assertThat(authorRepository.findById(initialAuthor.getId())).get().isEqualTo(author);
                assertAuthorRenamed(author, List.of(initialBookInfo.getBook().getId(), newBook.getId()));
            },
            () -> fail("Author was not renamed")
        );
    }

    private void assertAuthorRenamed(Author author, List<String> booksIds) {
        assertThat(author.hasFirstAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName())).isTrue();

        booksIds.forEach(
            bookId -> {
                var result = getInfoService.get(bookId);

                assertThat(result.isOk()).isTrue();

                assertThat(result.value()).get().satisfies(
                    bookInfo -> {
                        assertThat(bookInfo.getBook().getAuthors().get(0)).isEqualTo(author);

                        var comments = bookInfo.getComments();

                        if (!comments.isEmpty()) {
                            comments.forEach(
                                comment -> assertThat(comment.getBook().getAuthors().get(0)).isEqualTo(author)
                            );
                        }
                    }
                );
            }
        );
    }

    @DisplayName("возвращать ошибку при попытке переименовать автора в уже существующего")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void renameAuthorToExistingOne() {
        findInitial()
            .ifPresentOrElse(
                this::testRenamingToExistingAuthor,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    @DisplayName("возвращать пустой результат при попытке удалить несохраненного автора")
    @Test
    public void renameNonStored() {
        var result = service.removeAuthorById("");
        assertThat(result.isOk()).isTrue();
        assertThat(result.value()).isEmpty();
    }

    private void testRenamingToExistingAuthor(BookInfo initialBookInfo) {
        authorRepository.save(NEW_AUTHOR);

        var initialAuthor = initialBookInfo.getBook().getAuthors().get(0);

        assertThat(
            service.renameAuthor(
                new Author(
                    initialAuthor.getId(),
                    NEW_AUTHOR.getFirstName(),
                    NEW_AUTHOR.getLastName()
                )
            ).isOk()
        ).isFalse();
    }

    @DisplayName("удалить автора книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeAuthor() {
        findInitial()
            .ifPresentOrElse(
                this::testAuthorRemoved,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testAuthorRemoved(BookInfo initialBookInfo) {
        assertInitialBookInfo(initialBookInfo);

        var initialAuthor = initialBookInfo.getBook().getAuthors().get(0);

        var newBook = bookRepository.save(new Book(NEW_BOOK_TITLE).addAuthor(initialAuthor));

        assertThat(bookRepository.findAll().size()).isEqualTo(2);
        assertThat(newBook.getAuthors().get(0)).isEqualTo(initialAuthor);

        var result = service.removeAuthorById(initialAuthor.getId());

        assertThat(result.isOk()).isTrue();

        result.value().ifPresentOrElse(
            author -> {
                assertThat(authorRepository.findById(initialAuthor.getId())).isEmpty();
                assertAuthorRemoved(List.of(initialBookInfo.getBook().getId(), newBook.getId()));
            },
            () -> fail("Author was not renamed")
        );
    }

    private void assertAuthorRemoved(List<String> booksIds) {
        booksIds.forEach(
            bookId -> {
                var result = getInfoService.get(bookId);

                assertThat(result.isOk()).isTrue();

                assertThat(result.value()).get().satisfies(
                    bookInfo -> {
                        assertThat(bookInfo.getBook().getAuthors()).isEmpty();

                        var comments = bookInfo.getComments();

                        if (!comments.isEmpty()) {
                            comments.forEach(comment -> assertThat(comment.getBook().getAuthors()).isEmpty());
                        }
                    }
                );
            }
        );
    }

    @DisplayName("переименовать жанр книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void renameGenre() {
        findInitial()
            .ifPresentOrElse(
                this::testGenreRenaming,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testGenreRenaming(BookInfo initialBookInfo) {
        assertInitialBookInfo(initialBookInfo);

        var initialGenre = initialBookInfo.getBook().getGenres().get(0);

        var newBook = bookRepository.save(new Book(NEW_BOOK_TITLE).addGenre(initialGenre));

        assertThat(bookRepository.findAll().size()).isEqualTo(2);
        assertThat(newBook.getGenres().get(0)).isEqualTo(initialGenre);

        var result = service.renameGenre(new Genre(initialGenre.getId(), NEW_GENRE_NAME));

        assertThat(result.isOk()).isTrue();

        result.value().ifPresentOrElse(
            genre -> {
                assertThat(genreRepository.findById(initialGenre.getId())).get().isEqualTo(genre);
                assertGenreRenamed(genre, List.of(initialBookInfo.getBook().getId(), newBook.getId()));
            },
            () -> fail("Author was not renamed")
        );
    }

    private void assertGenreRenamed(Genre genre, List<String> booksIds) {
        assertThat(genre.hasName(NEW_GENRE_NAME)).isTrue();

        booksIds.forEach(
            bookId -> {
                var result = getInfoService.get(bookId);

                assertThat(result.isOk()).isTrue();

                assertThat(result.value()).get().satisfies(
                    bookInfo -> {
                        assertThat(bookInfo.getBook().getGenres().get(0)).isEqualTo(genre);

                        var comments = bookInfo.getComments();

                        if (!comments.isEmpty()) {
                            comments.forEach(
                                comment -> assertThat(comment.getBook().getGenres().get(0)).isEqualTo(genre)
                            );
                        }
                    }
                );
            }
        );
    }

    @DisplayName("удалить жанр книг")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeGenre() {
        findInitial()
            .ifPresentOrElse(
                this::testGenreRemoved,
                () -> fail(INITIAL_BOOK_NOT_FOUND_MESSAGE)
            );
    }

    private void testGenreRemoved(BookInfo initialBookInfo) {
        assertInitialBookInfo(initialBookInfo);

        var initialGenre = initialBookInfo.getBook().getGenres().get(0);

        var newBook = bookRepository.save(new Book(NEW_BOOK_TITLE).addGenre(initialGenre));

        assertThat(bookRepository.findAll().size()).isEqualTo(2);
        assertThat(newBook.getGenres().get(0)).isEqualTo(initialGenre);

        var result = service.removeGenreById(initialGenre.getId());

        assertThat(result.isOk()).isTrue();

        result
            .value()
            .ifPresentOrElse(
                author -> {
                    assertThat(genreRepository.findById(initialGenre.getId())).isEmpty();
                    assertGenreRemoved(List.of(initialBookInfo.getBook().getId(), newBook.getId()));
                },
                () -> fail("Author was not renamed")
            );
    }

    private void assertGenreRemoved(List<String> booksIds) {
        booksIds.forEach(
            bookId -> {
                var result = getInfoService.get(bookId);

                assertThat(result.isOk()).isTrue();

                assertThat(result.value()).get().satisfies(
                    bookInfo -> {
                        assertThat(bookInfo.getBook().getGenres()).isEmpty();

                        var comments = bookInfo.getComments();

                        if (!comments.isEmpty()) {
                            comments.forEach(comment -> assertThat(comment.getBook().getGenres()).isEmpty());
                        }
                    }
                );
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
        assertThat(
            book
                .getAuthors()
                .get(0)
                .hasFirstAndLastName(
                    INITIAL_AUTHOR.getFirstName(),
                    INITIAL_AUTHOR.getLastName()
                )
        ).isTrue();

        assertThat(book.getGenres().size()).isEqualTo(1);
        assertThat(book.getGenres().get(0).hasName(INITIAL_GENRE_NAME)).isTrue();
    }
}
