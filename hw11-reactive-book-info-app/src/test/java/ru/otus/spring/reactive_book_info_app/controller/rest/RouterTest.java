package ru.otus.spring.reactive_book_info_app.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.AuthorDto;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.BookDto;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.BookInfoDto;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.GenreDto;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.domain.Genre;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@DisplayName("Контроллер приложения должен")
@SpringBootTest
public class RouterTest {
    private static final String INITIAL_BOOK_TITLE = "Tri porosenka";
    private static final Author INITIAL_AUTHOR = new Author("Some", "Author");
    private static final Genre INITIAL_GENRE = new Genre("horror");
    private static final String INITIAL_COMMENT_TEXT = "Good book!";

    private static final String NEW_BOOK_TITLE = "Tri kotenka";
    private static final Author NEW_AUTHOR = new Author("Another", "One");
    private static final Genre NEW_GENRE = new Genre("drama");

    private static final String NEW_COMMENT_TEXT = "Super book!";

    @Autowired
    private RouterFunction<ServerResponse> router;

    @DisplayName("отдавать ответ с хранимыми книгами")
    @Test
    public void allBooks() {
        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .get()
            .uri("/books")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList(Book.class)
            .consumeWith(result -> assertInitialBookList(result.getResponseBody()))
        ;
    }

    private void assertInitialBookList(List<Book> books) {
        assertThat(books).isNotNull();
        assertThat(books).isInstanceOf(List.class);

        assertThat(books.size()).isEqualTo(1);

        var initialBook = books.get(0);

        assertThat(initialBook.getTitle()).isEqualTo(INITIAL_BOOK_TITLE);

        assertThat(initialBook.getAuthors()).hasSize(1);

        var actualAuthor = initialBook.getAuthors().get(0);

        assertThat(actualAuthor.getId()).isNotNull();
        assertThat(actualAuthor.getFirstName()).isEqualTo(INITIAL_AUTHOR.getFirstName());
        assertThat(actualAuthor.getLastName()).isEqualTo(INITIAL_AUTHOR.getLastName());

        assertThat(initialBook.getGenres()).hasSize(1);

        var actualGenre = initialBook.getGenres().get(0);

        assertThat(actualGenre.getId()).isNotNull();
        assertThat(actualGenre.getName()).isEqualTo(INITIAL_GENRE.getName());
    }

    @DisplayName("успешно сохранять новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addBook() {
        var client = WebTestClient.bindToRouterFunction(router).build();

        client
            .post()
            .uri("/book")
            .body(BodyInserters.fromFormData("title", NEW_BOOK_TITLE))
            .exchange()
            .expectStatus()
            .isOk()
        ;

        client
            .get()
            .uri("/books")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList(Book.class)
            .hasSize(2)
        ;
    }

    @DisplayName("успешно отдавать информацию о хранимой книге")
    @Test
    public void getBookInfo() {
        var initialBook = initialBook();

        checkInitialBook(
            initialBook,
            result -> assertThat(result.getResponseBody()).isEqualTo(expectedInitialBookInfoDto(initialBook))
        );
    }

    private BookInfoDto expectedInitialBookInfoDto(Book initialBook) {
        return
            new BookInfoDto(
                new BookDto(
                    initialBook.getId(),
                    initialBook.getTitle(),
                    initialBook
                        .getAuthors()
                        .stream()
                        .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                        .collect(Collectors.toList()),
                    initialBook
                        .getGenres()
                        .stream()
                        .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                        .collect(Collectors.toList())
                ),
                List.of(INITIAL_COMMENT_TEXT)
            );
    }

    private Book initialBook() {
        return
            Objects
                .requireNonNull(
                    WebTestClient
                        .bindToRouterFunction(router)
                        .build()
                        .get()
                        .uri("/books")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectHeader().contentType(APPLICATION_JSON)
                        .expectBodyList(Book.class)
                        .returnResult()
                        .getResponseBody()
                )
                .get(0)
            ;
    }

    @DisplayName("отдавать пустой ответ при запросе информации о несохраненной книге")
    @Test
    public void getNonStoredBookInfo() {
        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .get()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}").build("NoSuchId"))
            .exchange()
            .expectStatus()
            .isNotFound()
        ;
    }

    private void checkInitialBook(Book expected, Consumer<EntityExchangeResult<BookInfoDto>> assertResult) {
        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .get()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}").build(expected.getId()))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BookInfoDto.class)
            .consumeWith(assertResult);
    }

    @DisplayName("успешно редактировать книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void editBook() {
        var initialBook = initialBook();

        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .put()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}").build(initialBook.getId()))
            .body(BodyInserters.fromFormData("title", NEW_BOOK_TITLE))
            .exchange()
            .expectStatus()
            .isOk();

        initialBook.setTitle(NEW_BOOK_TITLE);

        checkInitialBook(
            initialBook,
            result -> assertThat(result.getResponseBody()).isEqualTo(expectedInitialBookInfoDto(initialBook))
        );
    }

    @DisplayName("удалять хранимую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void removeBook() {
        var client = WebTestClient.bindToRouterFunction(router).build();

        client
            .delete()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}").build(initialBook().getId()))
            .exchange()
            .expectStatus()
            .isOk();

        client
            .get()
            .uri("/books")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList(Book.class)
            .hasSize(0)
        ;
    }

    @DisplayName("успешно добавлять нового автора книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addNewAuthor() {
        var initialBook = initialBook();

        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .post()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}/author").build(initialBook.getId()))
            .body(
                BodyInserters
                    .fromFormData("firstName", NEW_AUTHOR.getFirstName())
                    .with("lastName", NEW_AUTHOR.getLastName())
            )
            .exchange()
            .expectStatus()
            .isOk();

        checkInitialBook(
            initialBook,
            result -> assertBookWithNewAuthor(Objects.requireNonNull(result.getResponseBody()))
        );
    }

    private void assertBookWithNewAuthor(BookInfoDto bookInfoDto) {
        assertThat(bookInfoDto.getBook().getAuthors()).hasSize(2);

        var initialAuthor = bookInfoDto.getBook().getAuthors().get(0);

        assertThat(initialAuthor.getId()).isNotNull();
        assertThat(initialAuthor.getFirstName()).isEqualTo(INITIAL_AUTHOR.getFirstName());
        assertThat(initialAuthor.getLastName()).isEqualTo(INITIAL_AUTHOR.getLastName());

        var newAuthor = bookInfoDto.getBook().getAuthors().get(1);

        assertThat(newAuthor.getId()).isNotNull();
        assertThat(newAuthor.getFirstName()).isEqualTo(NEW_AUTHOR.getFirstName());
        assertThat(newAuthor.getLastName()).isEqualTo(NEW_AUTHOR.getLastName());
    }

    @DisplayName("игнорировать добавление уже существующего автора книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addExistingAuthor() {
        var initialBook = initialBook();

        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .post()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}/author").build(initialBook.getId()))
            .body(
                BodyInserters
                    .fromFormData("firstName", INITIAL_AUTHOR.getFirstName())
                    .with("lastName", INITIAL_AUTHOR.getLastName())
            )
            .exchange()
            .expectStatus()
            .isOk();

        checkInitialBook(
            initialBook,
            result -> assertThat(result.getResponseBody()).isEqualTo(expectedInitialBookInfoDto(initialBook))
        );
    }

    @DisplayName("успешно добавлять новый жанр книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addNewGenre() {
        var initialBook = initialBook();

        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .post()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}/genre").build(initialBook.getId()))
            .body(BodyInserters.fromFormData("name", NEW_GENRE.getName()))
            .exchange()
            .expectStatus()
            .isOk();

        checkInitialBook(
            initialBook,
            result -> assertBookWithNewGenre(Objects.requireNonNull(result.getResponseBody()))
        );
    }

    @DisplayName("игнорировать добавление уже существующего жанра книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addExistingGenre() {
        var initialBook = initialBook();

        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .post()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}/genre").build(initialBook.getId()))
            .body(BodyInserters.fromFormData("name", INITIAL_GENRE.getName()))
            .exchange()
            .expectStatus()
            .isOk();

        checkInitialBook(
            initialBook,
            result -> assertThat(result.getResponseBody()).isEqualTo(expectedInitialBookInfoDto(initialBook))
        );
    }

    private void assertBookWithNewGenre(BookInfoDto bookInfoDto) {
        assertThat(bookInfoDto.getBook().getGenres()).hasSize(2);

        var initialGenre = bookInfoDto.getBook().getGenres().get(0);

        assertThat(initialGenre.getId()).isNotNull();
        assertThat(initialGenre.getName()).isEqualTo(INITIAL_GENRE.getName());
        assertThat(initialGenre.getName()).isEqualTo(INITIAL_GENRE.getName());

        var newGenre = bookInfoDto.getBook().getGenres().get(1);

        assertThat(newGenre.getId()).isNotNull();
        assertThat(newGenre.getName()).isEqualTo(NEW_GENRE.getName());
        assertThat(newGenre.getName()).isEqualTo(NEW_GENRE.getName());
    }

    @DisplayName("успешно добавлять комментарий к книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void addNewComment() {
        var initialBook = initialBook();

        WebTestClient
            .bindToRouterFunction(router)
            .build()
            .post()
            .uri(uriBuilder -> uriBuilder.path("/book/{id}/comment").build(initialBook.getId()))
            .body(BodyInserters.fromFormData("text", NEW_COMMENT_TEXT))
            .exchange()
            .expectStatus()
            .isOk();

        checkInitialBook(
            initialBook,
            result -> assertThat(
                Objects
                    .requireNonNull(result.getResponseBody())
                    .getComments()
            ).isEqualTo(List.of(INITIAL_COMMENT_TEXT, NEW_COMMENT_TEXT))
        );
    }
}
