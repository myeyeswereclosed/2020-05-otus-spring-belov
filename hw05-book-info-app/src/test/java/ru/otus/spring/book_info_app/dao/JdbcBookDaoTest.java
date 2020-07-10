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
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Dao для работы с книгами должен ")
@JdbcTest
@Import({JdbcBookDao.class, JdbcAuthorDao.class, JdbcGenreDao.class})
public class JdbcBookDaoTest {
    private static final Book FIRST_BOOK = new Book(1, "Tri porosenka");
    private static final String SECOND_BOOK_TITLE = "Tri kotenka";

    private static final String AUTHOR_NAME = "Test";
    private static final String AUTHOR_LAST_NAME = "Author";

    private static final String GENRE = "horror";

    @Autowired
    private JdbcBookDao dao;

    @Autowired
    private JdbcAuthorDao authorDao;

    @Autowired
    private JdbcGenreDao genreDao;

    @DisplayName("находить книгу по идентификатору, если та была сохранена")
    @Test
    public void findBook() {
        var book = dao.findById(FIRST_BOOK.getId());

        assertThat(dao.findAll().size()).isEqualTo(1);
        assertThat(book).isEqualTo(FIRST_BOOK);
    }

    @DisplayName("сохранять нового автора")
    @Test
    public void saveBook() {
        assertThat(dao.findAll().size()).isEqualTo(1);

        var book = dao.save(new Book(SECOND_BOOK_TITLE));

        assertThat(book.getId()).isEqualTo(2);
        assertThat(dao.findAll().size()).isEqualTo(2);
        assertThat(dao.findById(book.getId()).getTitle()).isEqualTo(SECOND_BOOK_TITLE);
    }

    @DisplayName("обновлять название книги")
    @Test
    public void updateBook() {
        var book = dao.findById(FIRST_BOOK.getId());

        dao.update(new Book(book.getId(), SECOND_BOOK_TITLE));

        assertThat(dao.findById(book.getId()).getTitle()).isEqualTo(SECOND_BOOK_TITLE);
    }

    @DisplayName("выбрасывать исключение при отсутствии идентификатора книги")
    @Test
    public void throwExceptionIfBookNotFound() {
        assertThrows(DataAccessException.class, () -> dao.findById(2L));
    }

    @DisplayName("удалять книгу")
    @Test
    public void deleteBook() {
        assertThat(dao.findAll().size()).isEqualTo(1);

        dao.delete(FIRST_BOOK.getId());

        assertThat(dao.findAll().isEmpty());
    }

    @DisplayName("отдавать список книг ")
    @Test
    public void findAll() {
        assertThat(dao.findAll().size()).isEqualTo(1);

        dao.save(new Book(SECOND_BOOK_TITLE));

        var books = dao.findAll();

        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0)).isEqualTo(FIRST_BOOK);
        assertThat(books.get(1).getTitle()).isEqualTo(SECOND_BOOK_TITLE);
    }

    @DisplayName("добавлять автора книги, если книга и автор сохранены")
    @Test
    public void addAuthor() {
        var author = authorDao.save(new Author(AUTHOR_NAME, AUTHOR_LAST_NAME));

        assertAuthor(author);

        dao.addAuthor(FIRST_BOOK.getId(), author);

        var bookAuthors = authorDao.findByBook(FIRST_BOOK);

        assertThat(bookAuthors.size()).isEqualTo(1);
        assertAuthor(bookAuthors.get(0));
    }

    @DisplayName("добавлять жанр, если книга и жанр сохранены")
    @Test
    public void addGenre() {
        var genre = genreDao.save(new Genre(GENRE));

        assertThat(genre.getName()).isEqualTo(GENRE);

        dao.addGenre(FIRST_BOOK.getId(), genre);

        var bookGenres = genreDao.findByBook(FIRST_BOOK);

        assertThat(bookGenres.size()).isEqualTo(1);
        assertThat(bookGenres.get(0).getName()).isEqualTo(GENRE);
    }

    private void assertAuthor(Author author) {
        assertThat(author.getFirstName()).isEqualTo(AUTHOR_NAME);
        assertThat(author.getLastName()).isEqualTo(AUTHOR_LAST_NAME);
    }
}
