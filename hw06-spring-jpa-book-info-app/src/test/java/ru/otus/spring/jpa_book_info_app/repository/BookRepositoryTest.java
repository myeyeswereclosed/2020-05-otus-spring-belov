package ru.otus.spring.jpa_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.repository.book.JpaBookRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий книг должен ")
@DataJpaTest
@Import(JpaBookRepository.class)
public class BookRepositoryTest {
    private final static Book INITIAL_BOOK = new Book(1, "Tri porosenka");

    private final static String NEW_BOOK_TITLE = "Tri kotenka";

    private final static Author AUTHOR = new Author("Ivan", "Kotov");
    private final static Author ANOTHER_AUTHOR = new Author("Oleg", "Kotov");

    private final static String GENRE_NAME = "animals";
    private final static Genre GENRE = new Genre(GENRE_NAME);

    private final static String ANOTHER_GENRE_NAME = "love-story";
    private final static Genre ANOTHER_GENRE = new Genre("love-story");

    private final static String INITIAL_COMMENT_TEXT = "Good book!";
    private final static String NEW_COMMENT_TEXT = "Super book!";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private JpaBookRepository repository;

    @DisplayName("сохранять новую книгу без данных")
    
    @Test
    public void saveNewBook() {
        var newBook = repository.save(new Book(NEW_BOOK_TITLE));

        assertThat(newBook.getId()).isEqualTo(2);
        assertThat(newBook.getAuthors()).isEmpty();
        assertThat(newBook.getGenres()).isEmpty();
    }

    @DisplayName("сохранять новую книгу c данными")
    @Test
    public void saveNewBookWithInfo() {
        assertThat(repository.findAll().size()).isEqualTo(1);

        var newBook = new Book(NEW_BOOK_TITLE);

       newBook
            .addAuthor(new Author(AUTHOR.getFirstName(), AUTHOR.getLastName()))
            .addGenre(new Genre(GENRE_NAME))
        ;

        var bookStored = repository.save(newBook);

        assertThat(repository.findAll().size()).isEqualTo(2);

        assertBookWithInfo(bookStored);
    }

    @DisplayName("обновлять данные по книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void updateBook() {
        assertThat(repository.findAll().size()).isEqualTo(1);

        INITIAL_BOOK
            .addAuthor(new Author(AUTHOR.getFirstName(), AUTHOR.getLastName()))
            .addGenre(new Genre(GENRE_NAME))
//            .addComment(new Comment(NEW_COMMENT_TEXT))
        ;

        var bookStored = repository.save(INITIAL_BOOK);

        assertThat(repository.findAll().size()).isEqualTo(1);

        assertBookWithInfo(bookStored);
    }

    private void assertBookWithInfo(Book book) {
        assertThat(book.getAuthors().size()).isEqualTo(1);
        assertThat(book.isWrittenBy(AUTHOR));

        assertThat(book.getGenres().size()).isEqualTo(1);
        assertThat(book.hasGenre(GENRE));
    }

    @DisplayName("находить книгу с ее данными")
    @Test
    public void findById() {
        var book = new Book(NEW_BOOK_TITLE);

        book
            .addAuthor(new Author(AUTHOR.getFirstName(), AUTHOR.getLastName()))
            .addAuthor(new Author(ANOTHER_AUTHOR.getFirstName(), ANOTHER_AUTHOR.getLastName()))
            .addGenre(new Genre(GENRE_NAME))
            .addGenre(new Genre(ANOTHER_GENRE_NAME))
        ;

        var newBook = repository.save(book);

        var bookFound = repository.findById(newBook.getId()).get();

        assertBookFoundById(bookFound);
    }

    private void assertBookFoundById(Book book) {
        assertThat(book.getTitle()).isEqualTo(NEW_BOOK_TITLE);

        assertThat(book.getAuthors().size()).isEqualTo(2);
        assertThat(book.isWrittenBy(AUTHOR)).isTrue();
        assertThat(book.isWrittenBy(ANOTHER_AUTHOR)).isTrue();

        assertThat(book.getGenres().size()).isEqualTo(2);
        assertThat(book.hasGenre(GENRE)).isTrue();
        assertThat(book.hasGenre(ANOTHER_GENRE)).isTrue();
    }

    @DisplayName("удалять книгу по идентификатору")
    @Test
    public void delete() {
        assertThat(repository.delete(INITIAL_BOOK.getId())).isTrue();
        assertThat(repository.findAll()).isEmpty();
    }

    @DisplayName("находить все книги")
    @Test
    public void findAll() {
        var book = new Book("Tri kotenka");

        book
            .addAuthor(new Author(AUTHOR.getFirstName(), AUTHOR.getLastName()))
            .addAuthor(new Author(ANOTHER_AUTHOR.getFirstName(), ANOTHER_AUTHOR.getLastName()))
            .addGenre(new Genre(GENRE_NAME))
            .addGenre(new Genre(ANOTHER_GENRE_NAME))
        ;

        repository.save(book);

        var books = repository.findAll();

        assertThat(books.size()).isEqualTo(2);
        assertThat(
            books
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList())
                .containsAll(Arrays.asList(INITIAL_BOOK.getTitle(), book.getTitle()))
        ).isTrue();
        assertThat(books.contains(book)).isTrue();
    }
}
