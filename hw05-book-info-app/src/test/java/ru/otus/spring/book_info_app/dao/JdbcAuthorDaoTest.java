package ru.otus.spring.book_info_app.dao;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import ru.otus.spring.book_info_app.dao.author.JdbcAuthorDao;
import ru.otus.spring.book_info_app.dao.book.JdbcBookDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Dao для работы с авторами должен ")
@JdbcTest
@Import({JdbcBookDao.class, JdbcAuthorDao.class})
public class JdbcAuthorDaoTest {
    private static final Author AUTHOR_STORED = new Author(1, "Some", "Author");
    private static final Author NEW_AUTHOR = new Author("Test", "Author");
    private static final String EDITED_NAME = "Great";

    private static final Book BOOK_STORED = new Book(1, "Tri porosenka");

    @Autowired
    private JdbcBookDao bookDao;

    @Autowired
    private JdbcAuthorDao dao;

    @DisplayName("находить автора по имени и фамилии, если он сохранен")
    @Test
    public void find() {
        var author = dao.findByFirstAndLastName(AUTHOR_STORED.getFirstName(), AUTHOR_STORED.getLastName());

        assertThat(author).isEqualTo(AUTHOR_STORED);
    }

    @DisplayName("сохранять автора")
    @Test
    public void save() {
        var newOne = dao.save(NEW_AUTHOR);

        assertThat(newOne).isEqualTo(NEW_AUTHOR);
    }

    @DisplayName("обновлять данные об авторе")
    @Test
    public void update() {
        var previousName = AUTHOR_STORED.getFirstName();
        AUTHOR_STORED.setFirstName(EDITED_NAME);

        dao.update(AUTHOR_STORED);

        var updated = dao.findByFirstAndLastName(EDITED_NAME, AUTHOR_STORED.getLastName());

        assertThat(updated.getId()).isEqualTo(AUTHOR_STORED.getId());

        AUTHOR_STORED.setFirstName(previousName);
        dao.update(AUTHOR_STORED);
    }

    @DisplayName("выбрасывать исключение, если автор не найден")
    @Test
    public void throwExceptionIfAuthorNotFound() {
        assertThrows(
            DataAccessException.class,
            () -> dao.findByFirstAndLastName(NEW_AUTHOR.getFirstName(), NEW_AUTHOR.getLastName())
        );
    }

    @DisplayName("удалять автора")
    @Test
    public void deleteBook() {
        dao.delete(AUTHOR_STORED.getId());

        assertThrows(
            DataAccessException.class,
            () -> dao.findByFirstAndLastName(AUTHOR_STORED.getFirstName(), AUTHOR_STORED.getLastName())
        );
    }

    @DisplayName("находить авторов книги")
    @Test
    public void addAuthor() {
        dao.save(NEW_AUTHOR);

        bookDao.addAuthor(BOOK_STORED.getId(), AUTHOR_STORED);
        bookDao.addAuthor(BOOK_STORED.getId(), NEW_AUTHOR);

        var authors = dao.findByBook(BOOK_STORED);

        assertThat(authors.size()).isEqualTo(2);
        assertThat(authors).contains(AUTHOR_STORED, NEW_AUTHOR);
    }

    @DisplayName("находить авторов, у которых есть написанные книги")
    @Test
    public void findAuthorsHavingBooks() {
        bookDao.addAuthor(BOOK_STORED.getId(), AUTHOR_STORED);
        dao.save(NEW_AUTHOR);

        assertThat(
            dao.findByFirstAndLastName(
                NEW_AUTHOR.getFirstName(),
                NEW_AUTHOR.getLastName()
            )
        ).isEqualTo(NEW_AUTHOR);

        var authors = dao.findAllWithBooks();

        assertThat(authors.size()).isEqualTo(1);
        assertThat(authors.get(0)).isEqualTo(Pair.of(AUTHOR_STORED, BOOK_STORED.getId()));
    }
}
