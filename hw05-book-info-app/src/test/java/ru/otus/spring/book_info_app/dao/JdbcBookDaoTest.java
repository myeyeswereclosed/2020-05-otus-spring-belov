package ru.otus.spring.book_info_app.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import ru.otus.spring.book_info_app.dao.author.JdbcAuthorDao;
import ru.otus.spring.book_info_app.dao.book.JdbcBookDao;
import ru.otus.spring.book_info_app.dao.genre.JdbcGenreDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.service.name_parser.NameParserImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Dao для работы с книгами должен ")
@JdbcTest
@Import({JdbcBookDao.class, JdbcAuthorDao.class, JdbcGenreDao.class, NameParserImpl.class})
public class JdbcBookDaoTest {
    private static final String FIRST_BOOK = "Tri porosenka";
    private static final String SECOND_BOOK = "Tri kotenka";

    private static final String AUTHOR_NAME = "Some";
    private static final String AUTHOR_LAST_NAME = "Author";

    private static final String GENRE = "horror";

    @Autowired
    private JdbcBookDao dao;

    @Autowired
    private JdbcAuthorDao authorDao;

    @Autowired
    private JdbcGenreDao genreDao;

    @Autowired
    private NameParserImpl parser;

    @DisplayName("сохранять новую книгу ")
    @Test
    public void saveBook() {
        assertThat(dao.findAll().isEmpty());

        var book = dao.save(FIRST_BOOK);

        assertThat(book.getId()).isEqualTo(1);
        assertThat(dao.findAll().size()).isEqualTo(1);
        assertThat(dao.findById(book.getId()).getTitle()).isEqualTo(FIRST_BOOK);
    }

    @DisplayName("обновлять название книги")
    @Test
    public void updateBook() {
        var book = dao.save(FIRST_BOOK);

        dao.update(book.getId(), SECOND_BOOK);

        assertThat(dao.findById(book.getId()).getTitle()).isEqualTo(SECOND_BOOK);
    }

    @DisplayName("выбрасывать исключение при отсутствии идентификатора книги")
    @Test
    public void throwExceptionIfBookNotFound() {
        assertThrows(DataAccessException.class, () -> dao.findById(2L));
    }

    @DisplayName("удалять книгу")
    @Test
    public void deleteBook() {
        var book = dao.save(FIRST_BOOK);

        assertThat(dao.findAll().size()).isEqualTo(1);

        dao.delete(book.getId());

        assertThat(dao.findAll().isEmpty());
    }

    @DisplayName("отдавать список книг ")
    @Test
    public void findAll() {
        assertThat(dao.findAll().isEmpty());

        dao.save(FIRST_BOOK);
        dao.save(SECOND_BOOK);

        var books = dao.findAll();

        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0).getTitle()).isEqualTo(FIRST_BOOK);
        assertThat(books.get(1).getTitle()).isEqualTo(SECOND_BOOK);
    }

    @DisplayName("добавлять автора книги, если книга и автор сохранены ")
    @Test
    public void addAuthor() {
        assertThat(dao.findAll().isEmpty());

        var book = dao.save(FIRST_BOOK);
        var author = authorDao.save(AUTHOR_NAME + " " + AUTHOR_LAST_NAME);

        assertAuthor(author);

        dao.addAuthor(book.getId(), author);

        var bookAuthors = authorDao.findByBook(book);

        assertThat(bookAuthors.size()).isEqualTo(1);
        assertAuthor(bookAuthors.get(0));
    }

    @DisplayName("добавлять жанр, если книга и жанр сохранены ")
    @Test
    public void addGenre() {
        assertThat(dao.findAll().isEmpty());

        var book = dao.save(FIRST_BOOK);
        var genre = genreDao.save(GENRE);

        assertThat(genre.getName()).isEqualTo(GENRE);

        dao.addGenre(book.getId(), genre);

        var bookGenres = genreDao.findByBook(book);

        assertThat(bookGenres.size()).isEqualTo(1);
        assertThat(bookGenres.get(0).getName()).isEqualTo(GENRE);
    }

    private void assertAuthor(Author author) {
        assertThat(author.getName().getFirstName()).isEqualTo(AUTHOR_NAME);
        assertThat(author.getName().getLastName()).isEqualTo(AUTHOR_LAST_NAME);
    }
}
