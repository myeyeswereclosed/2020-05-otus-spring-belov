package ru.otus.spring.mongo_db_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.repository.book.BookRepository;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentScan(
    {
        "ru.otus.spring.mongo_db_book_info_app.migration",
        "ru.otus.spring.mongo_db_book_info_app.repository",
        "ru.otus.spring.mongo_db_book_info_app.config"
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

    @DisplayName("сохранять новую книгу без данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void saveNewBook() {
        assertInitialBook();

        var newBook = repository.save(new Book(NEW_BOOK_TITLE));

        assertThat(newBook.getAuthors()).isEmpty();
        assertThat(newBook.getGenres()).isEmpty();
    }

    @DisplayName("сохранять новую книгу c данными")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void saveNewBookWithInfo() {
        assertInitialBook();

        var bookStored = repository.save(createNewBook());

        assertThat(repository.findAll().size()).isEqualTo(2);
        assertNewBook(bookStored);
    }

    @DisplayName("мержить данные по книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void updateBook() {
        assertInitialBook();

        var newBook =
            repository.save(
                new Book(NEW_BOOK_TITLE)
                    .addAuthor(new Author(AUTHOR.getFirstName(), AUTHOR.getLastName()))
                    .addGenre(new Genre(GENRE_NAME))
            );

        var updatedBook =
            repository.save(
                newBook
                    .addAuthor(new Author(ANOTHER_AUTHOR.getFirstName(), ANOTHER_AUTHOR.getLastName()))
                    .addGenre(new Genre(ANOTHER_GENRE_NAME))
                );

        assertThat(repository.findAll().size()).isEqualTo(2);
        assertNewBook(updatedBook);
    }

    @DisplayName("не обновлять данные по несохраненной книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void updateNonStoredBookData() {
        assertInitialBook();

        assertThat(repository.update(new Book("NonStoredId", "No Title"))).isEmpty();
        assertThat(repository.findAll().size()).isEqualTo(1);

        // save() vs update()
        repository.save(new Book("NonStoredId", "No Title"));

        assertThat(repository.findAll().size()).isEqualTo(2);
    }

    @DisplayName("обновлять данные по сохраненной книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void updateStoredBookData() {
        assertInitialBook();

        var initialBook = repository.findAll().get(0);

        assertThat(repository.update(initialBook.changeTitle(NEW_BOOK_TITLE))).isPresent();

        assertInitialBook(NEW_BOOK_TITLE);
    }

    @DisplayName("находить книгу с ее данными")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void findById() {
        var newBook = repository.save(createNewBook());

        var bookFound = repository.findById(newBook.getId());

        assertThat(bookFound).get().satisfies(this::assertNewBook);
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

    private void assertNewBook(Book book) {
        assertThat(book.getTitle()).isEqualTo(NEW_BOOK_TITLE);

        assertThat(book.getAuthors().size()).isEqualTo(2);
        assertThat(book.isWrittenBy(AUTHOR)).isTrue();
        assertThat(book.isWrittenBy(ANOTHER_AUTHOR)).isTrue();

        assertThat(book.getGenres().size()).isEqualTo(2);
        assertThat(book.hasGenre(GENRE)).isTrue();
        assertThat(book.hasGenre(ANOTHER_GENRE)).isTrue();
    }

    @DisplayName("удалять книгу по идентификатору")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void delete() {
        assertInitialBook();

        var bookStored = repository.save(new Book(NEW_BOOK_TITLE));

        assertThat(repository.delete(bookStored.getId())).get().isEqualTo(bookStored.getId());
        assertInitialBook();
    }

    @DisplayName("отдавать пустой результат при попытке удаления по несуществующему идентификатору")
    @Test
    public void deleteNonStored() {
        assertInitialBook();

        assertThat(repository.delete("")).isEmpty();

        assertInitialBook();
    }

    @DisplayName("находить все книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    public void findAll() {
        assertInitialBook();

        var newBook = repository.save(createNewBook());

        var books = repository.findAll();

        assertThat(books.size()).isEqualTo(2);
        assertThat(
            books
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList())
                .containsAll(Arrays.asList(INITIAL_BOOK, newBook.getTitle()))
        ).isTrue();
        assertThat(repository.findById(newBook.getId())).get().satisfies(this::assertNewBook);
    }

    private void assertInitialBook() {
        assertInitialBook(INITIAL_BOOK);
    }

    private void assertInitialBook(String expectedTitle) {
        var books = repository.findAll();

        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getTitle()).isEqualTo(expectedTitle);
    }
}
