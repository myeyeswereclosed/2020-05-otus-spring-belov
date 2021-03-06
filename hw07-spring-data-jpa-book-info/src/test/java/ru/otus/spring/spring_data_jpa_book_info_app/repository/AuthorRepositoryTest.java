package ru.otus.spring.spring_data_jpa_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookAuthor;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.book.BookRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий авторов должен ")
@DataJpaTest
public class AuthorRepositoryTest {
    private static final Author INITIAL_AUTHOR = new Author(1, "Some", "Author");

    private static final Author NEW_AUTHOR = new Author("Ivan", "Kotov");

    private static final Book INITIAL_BOOK = new Book(1, "Tri porosenka");

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("находить сохраненных авторов")
    @Test
    public void findAll() {
        var authors = repository.findAll();

        assertThat(authors.size()).isEqualTo(1);

        assertThat(authors.get(0)).isEqualTo(INITIAL_AUTHOR);
    }

    @DisplayName("сoхранять нового автора")
    @Test
    public void save() {
        assertTestPreconditions();

        var newAuthor = repository.save(NEW_AUTHOR);

        assertThat(newAuthor.getId()).isEqualTo(2);
        assertThat(newAuthor).isEqualTo(NEW_AUTHOR);
    }

    @DisplayName("обновлять данные автора")
    @Test
    public void update() {
        assertTestPreconditions();

        var author = new Author(1, "Oleg", "Krotov");

        var stored = repository.save(author);

        assertThat(stored).isEqualTo(author);
    }

    @DisplayName("удалять автора")
    @Test
    public void delete() {
        repository.deleteById(INITIAL_AUTHOR.getId());

        assertThat(repository.findAll()).isEmpty();
    }

    @DisplayName("находить автора по имени и фамилии")
    @Test
    public void findByFirstNameAndLastName() {
        assertTestPreconditions();

        var author =
            repository
                .findByFirstNameAndLastName(
                    INITIAL_AUTHOR.getFirstName(),
                    INITIAL_AUTHOR.getLastName()
                ).get();

        assertThat(author).isEqualTo(INITIAL_AUTHOR);
    }

    @DisplayName("отдавать пустой результат если автор с запрошенными данными не найден")
    @Test
    public void emptyByFirstNameAndLastName() {
        assertTestPreconditions();

        assertThat(
            repository
                .findByFirstNameAndLastName(
                    INITIAL_AUTHOR.getLastName(),
                    INITIAL_AUTHOR.getLastName()
                )
        ).isEmpty();
    }

    @DisplayName("находить авторов, у которых есть книги")
    @Test
    public void findWithBooks() {
        assertTestPreconditions();

        repository.save(NEW_AUTHOR);

        var authorsWithBooks = repository.findAllWithBooks();

        assertThat(authorsWithBooks.size()).isEqualTo(1);
        assertBookAuthor(authorsWithBooks.get(0));
    }

    private void assertBookAuthor(BookAuthor bookAuthor) {
        assertThat(bookAuthor.getAuthorId()).isEqualTo(INITIAL_AUTHOR.getId());
        assertThat(bookAuthor.getAuthorFirstName()).isEqualTo(INITIAL_AUTHOR.getFirstName());
        assertThat(bookAuthor.getAuthorLastName()).isEqualTo(INITIAL_AUTHOR.getLastName());
        assertThat(bookAuthor.getBookId()).isEqualTo(INITIAL_BOOK.getId());
    }

    private void assertTestPreconditions() {
        var books = bookRepository.findAll();
        var authors = repository.findAll();

        assertThat(books.size()).isEqualTo(1);
        assertThat(authors.size()).isEqualTo(1);

        var book = books.get(0);
        var author = authors.get(0);

        assertThat(author).isEqualTo(INITIAL_AUTHOR);

        assertThat(book.getId()).isEqualTo(INITIAL_BOOK.getId());
        assertThat(book.getTitle()).isEqualTo(INITIAL_BOOK.getTitle());
        assertThat(book.getAuthors()).isEqualTo(Set.of(INITIAL_AUTHOR));
    }
}
