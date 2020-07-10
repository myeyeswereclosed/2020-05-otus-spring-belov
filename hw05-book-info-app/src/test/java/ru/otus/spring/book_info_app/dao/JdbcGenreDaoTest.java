package ru.otus.spring.book_info_app.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import ru.otus.spring.book_info_app.dao.book.JdbcBookDao;
import ru.otus.spring.book_info_app.dao.genre.JdbcGenreDao;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Dao для работы с жанрами должен ")
@JdbcTest
@Import({JdbcBookDao.class, JdbcGenreDao.class})
public class JdbcGenreDaoTest {
    private static final Genre GENRE_STORED = new Genre(1, "science");
    private static final Genre NEW_GENRE = new Genre(3, "horror");
    private static final String EDITED_GENRE = "love-story";

    private static final Book BOOK_STORED = new Book(1, "Tri porosenka");

    @Autowired
    private JdbcBookDao bookDao;

    @Autowired
    private JdbcGenreDao dao;

    @DisplayName("находить жанр по названию, если он сохранен")
    @Test
    public void find() {
        var author = dao.findByName(GENRE_STORED.getName());

        assertThat(author).isEqualTo(GENRE_STORED);
    }

    @DisplayName("сохранять новый жанр")
    @Test
    public void save() {
        var newOne = dao.save(NEW_GENRE);

        assertThat(newOne).isEqualTo(NEW_GENRE);
    }

    @DisplayName("обновлять название")
    @Test
    public void update() {
        var previousName = GENRE_STORED.getName();
        GENRE_STORED.setName(EDITED_GENRE);

        dao.update(GENRE_STORED);

        var updated = dao.findByName(EDITED_GENRE);

        assertThat(updated.getId()).isEqualTo(GENRE_STORED.getId());

        GENRE_STORED.setName(previousName);
        dao.update(GENRE_STORED);
    }

    @DisplayName("выбрасывать исключение, если жанр не найден")
    @Test
    public void throwExceptionIfAuthorNotFound() {
        assertThrows(
            DataAccessException.class,
            () -> dao.findByName(NEW_GENRE.getName())
        );
    }

    @DisplayName("удалять жанр")
    @Test
    public void deleteBook() {
        dao.delete(GENRE_STORED.getId());

        assertThrows(
            DataAccessException.class,
            () -> dao.findByName(GENRE_STORED.getName())
        );
    }

    @DisplayName("находить жанры книги")
    @Test
    public void addAuthor() {
        dao.save(NEW_GENRE);

        bookDao.addGenre(BOOK_STORED.getId(), GENRE_STORED);
        bookDao.addGenre(BOOK_STORED.getId(), NEW_GENRE);

        var authors = dao.findByBook(BOOK_STORED);

        assertThat(authors.size()).isEqualTo(2);
        assertThat(authors).contains(GENRE_STORED, NEW_GENRE);
    }
}
