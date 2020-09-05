package ru.otus.spring.reactive_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.domain.Genre;
import ru.otus.spring.reactive_book_info_app.repository.book.BookRepository;

@ComponentScan(
    {
        "ru.otus.spring.reactive_book_info_app.migration",
        "ru.otus.spring.reactive_book_info_app.repository",
        "ru.otus.spring.reactive_book_info_app.config"
    }
)
@DisplayName("Репозиторий книг должен ")
@DataMongoTest
public class BookRepositoryTest {
    private static final String INITIAL_BOOK = "Tri porosenka";

    private final static String NEW_BOOK_TITLE = "Tri kotenka";

    private final static Author AUTHOR = new Author("Ivan", "Kotov");
    private final static Author ANOTHER_AUTHOR = new Author("Oleg", "Kotov");

    private final static String GENRE_NAME = "animals";
    private final static Genre GENRE = new Genre(GENRE_NAME);

    private final static String ANOTHER_GENRE_NAME = "love-story";
    private final static Genre ANOTHER_GENRE = new Genre("love-story");

    @Autowired
    private BookRepository repository;

    @DisplayName("находить книги инитной миграции")
    @Test
    public void findInitial() {
        assertInitialBook();
    }

    @DisplayName("сохранять новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void saveNewBook() {
        assertInitialBook();

        var newBook = repository.save(createNewBook());

        StepVerifier
            .create(newBook)
            .expectNextMatches(this::assertNewBook)
            .verifyComplete()
        ;
    }

    @DisplayName("не обновлять данные по несохраненной книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void updateNonStoredBookData() {
        assertInitialBook();

        StepVerifier
            .create(repository.update(new Book("NonStoredId", "No Title")))
            .verifyComplete();
    }

    @DisplayName("обновлять данные по сохраненной книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void updateStoredBookData() {
        assertInitialBook();

        var booksFlux = repository.findAll();

        var initialBook = booksFlux.collectList().block().get(0);

        StepVerifier
            .create(repository.update(new Book(initialBook.getId(), NEW_BOOK_TITLE)))
            .expectNextMatches(book -> book.getId().equals(initialBook.getId()))
            .verifyComplete()
        ;

        assertInitialBook(NEW_BOOK_TITLE);
    }

    @DisplayName("находить книгу с ее данными")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void findById() {
        assertInitialBook();

        var newBook = repository.save(createNewBook()).block();

        StepVerifier
            .create(repository.findById(newBook.getId()))
            .expectNextMatches(this::assertNewBook)
            .verifyComplete()
        ;
    }

    private Book createNewBook() {
        return
            new Book(NEW_BOOK_TITLE)
                .addAuthor(new Author(AUTHOR.getFirstName(), AUTHOR.getLastName()))
                .addAuthor(new Author(ANOTHER_AUTHOR.getFirstName(), ANOTHER_AUTHOR.getLastName()))
                .addGenre(new Genre(GENRE_NAME))
                .addGenre(new Genre(ANOTHER_GENRE_NAME))
        ;
    }

    private boolean assertNewBook(Book book) {
        return
            book.getTitle().equals(NEW_BOOK_TITLE)
                &&
            book.getAuthors().size() == 2
                &&
            book.isWrittenBy(AUTHOR)
                &&
            book.isWrittenBy(ANOTHER_AUTHOR)
                &&
            book.getGenres().size() == 2
                &&
            book.hasGenre(GENRE)
                &&
            book.hasGenre(ANOTHER_GENRE)
        ;
    }

    @DisplayName("удалять книгу по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void delete() {
        assertInitialBook();

        var initialBook = repository.findAll().collectList().block().get(0);

        StepVerifier
            .create(repository.delete(initialBook.getId()))
            .expectNextMatches(id -> id.equals(initialBook.getId()))
            .verifyComplete()
        ;

        StepVerifier
            .create(repository.findAll())
            .expectNextCount(0)
            .verifyComplete()
        ;
    }

    @DisplayName("отдавать пустой результат при попытке удаления по несуществующему идентификатору")
    @Test
    public void deleteNonStored() {
        assertInitialBook();

        StepVerifier
            .create(repository.delete(""))
            .expectNextCount(0)
            .verifyComplete();
    }

    @DisplayName("находить все книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void findAll() {
        assertInitialBook();

        repository.save(createNewBook()).block();

        StepVerifier
            .create(repository.findAll())
            .expectNextMatches(book -> book.getTitle().equals(INITIAL_BOOK))
            .expectNextMatches(this::assertNewBook)
            .verifyComplete()
        ;
    }

    private void assertInitialBook() {
        assertInitialBook(INITIAL_BOOK);
    }

    private void assertInitialBook(String expectedTitle) {
        StepVerifier
            .create(repository.findAll())
            .expectNextMatches(book -> book.getTitle().equals(expectedTitle))
            .verifyComplete()
        ;
    }
}
