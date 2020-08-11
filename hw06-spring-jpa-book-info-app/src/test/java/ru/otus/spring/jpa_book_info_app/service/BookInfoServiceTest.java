package ru.otus.spring.jpa_book_info_app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.dto.BookInfo;
import ru.otus.spring.jpa_book_info_app.repository.author.JpaAuthorRepository;
import ru.otus.spring.jpa_book_info_app.repository.book.JpaBookRepository;
import ru.otus.spring.jpa_book_info_app.repository.comment.JpaCommentRepository;
import ru.otus.spring.jpa_book_info_app.repository.genre.JpaGenreRepository;
import ru.otus.spring.jpa_book_info_app.service.book.BookInfoServiceImpl;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис сводной информации о книгах должен ")
@SpringBootTest
@Import({JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class, JpaCommentRepository.class})
public class BookInfoServiceTest {

    private static final Author INITIAL_AUTHOR = new Author(1, "Some", "Author");
    private static final Genre INITIAL_GENRE = new Genre(1, "horror");

    private static final Book INITIAL_BOOK = new Book(1, "Tri porosenka");

    private static final Comment INITIAL_COMMENT = new Comment(1, "Good book!", INITIAL_BOOK);

    private static final Author NEW_AUTHOR = new Author(2, "Oleg", "Kotov");
    private static final Genre NEW_GENRE = new Genre(3, "love-story");
    private static final Comment NEW_COMMENT = new Comment(2, "Super book!");

    @Autowired
    private JpaAuthorRepository authorRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Autowired
    private BookInfoServiceImpl service;

    @DisplayName("находить книгу по идентификатору")
    @Test
    public void get() {
        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.value()).hasValueSatisfying(this::assertInitialBookInfo);
    }

    @DisplayName("отдавать пустой результат, если не книга не найдена")
    @Test
    public void emptyIfNotFound() {
        var result = service.get(0);

        assertThat(result.isOk());
        assertThat(result.value().isEmpty()).isTrue();
    }

    @DisplayName("успешно добавлять нового автора к хранимой книге")
    @Test
    public void addNewBookAuthor() {
        assertThat(
            service.addBookAuthor(
                INITIAL_BOOK.getId(),
                new Author(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName())
            ).isOk()
        ).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(BookInfo::getAuthors)
            .isEqualTo(Set.of(INITIAL_AUTHOR, NEW_AUTHOR))
        ;
    }

    @DisplayName("игнорировать добавление уже существующего автора книги")
    @Test
    public void addExistingBookAuthor() {
        assertThat(
            service.addBookAuthor(
                INITIAL_BOOK.getId(),
                new Author(INITIAL_AUTHOR.getFirstName(), INITIAL_AUTHOR.getLastName())
            ).isOk()
        ).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(BookInfo::getAuthors)
            .isEqualTo(Set.of(INITIAL_AUTHOR))
        ;
    }

    @DisplayName("успешно добавлять сохраненного автора как автора книги")
    @Transactional
    @Test
    public void addStoredAuthorAsBookAuthor() {
        var author = new Author(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName());

        authorRepository.save(author);

        assertThat(service.addBookAuthor(INITIAL_BOOK.getId(), author).isOk()).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(BookInfo::getAuthors)
            .isEqualTo(Set.of(INITIAL_AUTHOR, NEW_AUTHOR))
        ;
    }

    @DisplayName("успешно добавлять новый жанр к хранимой книге")
    @Test
    public void addNewBookGenre() {
        assertThat(
            service.addBookGenre(
                INITIAL_BOOK.getId(),
                new Genre(NEW_GENRE.getName())
            ).isOk()
        ).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(BookInfo::getGenres)
            .isEqualTo(Set.of(INITIAL_GENRE, NEW_GENRE))
        ;
    }

    @DisplayName("игнорировать добавление уже существующего жанра книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void addExistingBookGenre() {
        assertThat(
            service.addBookGenre(INITIAL_BOOK.getId(), new Genre(INITIAL_GENRE.getName())).isOk()
        ).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(BookInfo::getGenres)
            .isEqualTo(Set.of(INITIAL_GENRE))
        ;
    }

    @DisplayName("успешно добавлять сохраненный жанр как жанр книги")
    @Transactional
    @Test
    public void addStoredGenreAsBookGenre() {
        var genre = new Genre(NEW_GENRE.getName());

        genreRepository.save(genre);

        assertThat(service.addBookGenre(INITIAL_BOOK.getId(), genre).isOk()).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .get()
            .extracting(BookInfo::getGenres)
            .isEqualTo(Set.of(INITIAL_GENRE, NEW_GENRE))
        ;
    }

    @DisplayName("добавлять комментарий к книге")
    @Test
    public void addComment() {
        assertThat(
            service.addComment(INITIAL_BOOK.getId(), new Comment(NEW_COMMENT.getText())).isOk()
        ).isTrue();

        var result = service.get(INITIAL_BOOK.getId());

        assertThat(result.isOk()).isTrue();

        assertThat(result.value())
            .hasValueSatisfying(
                bookInfo ->
                    assertThat(
                        bookInfo
                            .getComments()
                            .stream()
                            .sorted(Comparator.comparingLong(Comment::getId))
                            .map(Comment::getText)
                            .collect(Collectors.toList())
                    )
                        .isEqualTo(List.of(INITIAL_COMMENT.getText(), NEW_COMMENT.getText()))
            )
        ;
    }

    @DisplayName("находить все книги с информацией по ним")
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void getAll() {
        var result = service.getAll();

        assertThat(result.isOk()).isTrue();
        assertThat(result.value())
            .hasValueSatisfying(
                bookInfoList -> {
                    assertThat(bookInfoList.size()).isEqualTo(1);
                    assertInitialBookInfo(bookInfoList.get(0));
                }
            );
    }

    private void assertInitialBookInfo(BookInfo bookInfo) {
        assertInitialBook(bookInfo);

        var authors = bookInfo.getAuthors();

        assertThat(authors.size()).isEqualTo(1);

        var author = List.copyOf(authors).get(0);

        assertThat(author).isEqualTo(INITIAL_AUTHOR);

        var genres = bookInfo.getGenres();

        assertThat(genres.size()).isEqualTo(1);

        var genre = List.copyOf(genres).get(0);

        assertThat(genre).isEqualTo(INITIAL_GENRE);

        assertThat(bookInfo.getComments().size()).isEqualTo(1);

        var comment = List.copyOf(bookInfo.getComments()).get(0);

        assertThat(comment).satisfies(
            actual -> {
                assertThat(actual.getId()).isEqualTo(INITIAL_COMMENT.getId());
                assertThat(actual.getText()).isEqualTo(INITIAL_COMMENT.getText());
                assertInitialBook(actual.getBook());
            }
        );
    }

    private void assertInitialBook(Book book) {
        assertThat(book.getId()).isEqualTo(INITIAL_BOOK.getId());
        assertThat(book.getTitle()).isEqualTo(INITIAL_BOOK.getTitle());
    }

    private void assertInitialBook(BookInfo book) {
        assertThat(book.getId()).isEqualTo(INITIAL_BOOK.getId());
        assertThat(book.getTitle()).isEqualTo(INITIAL_BOOK.getTitle());
    }
}
