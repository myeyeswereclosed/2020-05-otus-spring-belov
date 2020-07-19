package ru.otus.spring.jpa_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.jpa_book_info_app.domain.Book;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.dto.BookGenre;
import ru.otus.spring.jpa_book_info_app.repository.book.JpaBookRepository;
import ru.otus.spring.jpa_book_info_app.repository.genre.JpaGenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий жанров должен ")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({JpaGenreRepository.class, JpaBookRepository.class})
public class GenreRepositoryTest {
    private static final List<Genre> INITIAL_GENRES = List.of(new Genre(1, "horror"), new Genre(2, "history"));

    private static final Genre NEW_GENRE = new Genre("love-story");
    private static final Genre UPDATED_NEW_GENRE = new Genre(3, "drama");

    private static final Book INITIAL_BOOK = new Book(1, "Tri porosenka");

    @Autowired
    TestEntityManager em;

    @Autowired
    JpaGenreRepository repository;

    @Autowired
    JpaBookRepository bookRepository;

    @DisplayName("находить сохраненные жанры")
    @Test
    public void findAll() {
        var genres = repository.findAll();

        assertGenres(INITIAL_GENRES, genres);
    }

    @DisplayName("сoхранять новый жанр")
    @Test
    public void save() {
        assertTestPreconditions();

        var newGenre = repository.save(NEW_GENRE);

        assertThat(newGenre.getId()).isEqualTo(3);
        assertThat(newGenre).isEqualTo(NEW_GENRE);
    }

    @DisplayName("обновлять название жанра")
    @Test
    public void update() {
        assertTestPreconditions();

        var newGenre = repository.save(NEW_GENRE);
        newGenre.setName(UPDATED_NEW_GENRE.getName());

        repository.save(newGenre);

        assertThat(newGenre).isEqualTo(UPDATED_NEW_GENRE);
    }

    @DisplayName("удалять жанр")
    @Test
    public void delete() {
        assertTestPreconditions();

        assertThat(repository.delete(INITIAL_GENRES.get(0).getId())).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(1);

        assertThat(repository.delete(INITIAL_GENRES.get(1).getId())).isTrue();
        assertThat(repository.findAll()).isEmpty();
    }

    @DisplayName("находить жанр по названию")
    @Test
    public void findByName() {
        assertTestPreconditions();

        var bookGenre = INITIAL_GENRES.get(0);

        assertThat(repository.findByName(bookGenre.getName()).get()).isEqualTo(bookGenre);
    }

    @DisplayName("отдавать пустой результат, если жанр с запрошенным названием не найден")
    @Test
    public void emptyByFirstNameAndLastName() {
        assertTestPreconditions();

        assertThat(repository.findByName("No such genre")).isEmpty();
    }

    @DisplayName("находить жанры, относящиеся к хранимым книгам")
    @Test
    public void findWithBooks() {
        assertTestPreconditions();

        repository.save(NEW_GENRE);

        var booksGenres = repository.findAllWithBooks();

        assertThat(booksGenres.size()).isEqualTo(1);
        assertBookGenre(INITIAL_GENRES.get(0), booksGenres.get(0));
    }

    private void assertTestPreconditions() {
        var books = bookRepository.findAll();
        var genres = repository.findAll();

        assertThat(books.size()).isEqualTo(1);
        assertThat(genres.size()).isEqualTo(2);

        assertGenres(INITIAL_GENRES, genres);

        var book = books.get(0);
        var bookGenre = genres.get(0);

        assertThat(book.getGenres().size()).isEqualTo(1);
        assertThat(book.hasGenre(bookGenre));
    }

    private void assertGenres(List<Genre> expectedGenres, List<Genre> actualGenres) {
        assertThat(actualGenres.size()).isEqualTo(expectedGenres.size());

        for (var i = 0; i < actualGenres.size(); i++) {
            assertThat(actualGenres.get(i)).isEqualTo(expectedGenres.get(i));
        }
    }

    private void assertBookGenre(Genre expectedGenre, BookGenre bookGenre) {
        assertThat(bookGenre.getBookId()).isEqualTo(INITIAL_BOOK.getId());
        assertThat(bookGenre.getGenreId()).isEqualTo(expectedGenre.getId());
        assertThat(bookGenre.getGenreName()).isEqualTo(expectedGenre.getName());
    }
}