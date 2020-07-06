package ru.otus.spring.book_info_app.dao.book;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.book_info_app.dao.mapper.BookMapper;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonMap;

@Repository
public class JdbcBookDao implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    public JdbcBookDao(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public Book save(String title) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(
            "insert into book(title) values(:title)",
            new MapSqlParameterSource().addValue("title", title),
            keyHolder,
            new String[]{"id"}
        );

        return new Book(Objects.requireNonNull(keyHolder.getKey()).longValue(), title);
    }

    @Override
    public Book findById(long id) {
        return
            jdbc
                .queryForObject(
                    "select id, title from book where id = :id",
                    singletonMap("id", id),
                    new BookMapper()
                );
    }

    public void update(long id, String title) {
        jdbc.update(
            "update book set title = :title where id = :id",
            Map.of("title", title, "id", id)
        );
    }

    @Override
    public void delete(long id) {
        jdbc.update(
            "delete from book where id = :id",
            Map.of("id", id)
        );
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("select id, title from book", new BookMapper());
    }

    @Override
    public void addAuthor(long id, Author author) {
        jdbc.update(
            "insert into book_author(book_id, author_id) values(:bookId, :authorId)",
            Map.of("bookId", id, "authorId", author.getId())
        );
    }

    @Override
    public void addGenre(long id, Genre genre) {
        jdbc.update(
            "insert into book_genre(book_id, genre_id) values(:bookId, :genreId)",
            Map.of("bookId", id, "genreId", genre.getId())
        );
    }
}