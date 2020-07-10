package ru.otus.spring.book_info_app.dao.author;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.book_info_app.dao.mapper.AuthorMapper;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class JdbcAuthorDao implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    public JdbcAuthorDao(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public Author findByFirstAndLastName(String firstName, String lastName) {
        return
            jdbc.queryForObject(
                "select id, first_name, last_name from author where first_name = :firstName and last_name = :lastName",
                Map.of("firstName", firstName, "lastName", lastName),
                new AuthorMapper()
            );
    }

    public Author save(Author author) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(
            "insert into author(first_name, last_name) values(:firstName, :lastName)",
            new MapSqlParameterSource()
                .addValue("firstName", author.getFirstName())
                .addValue("lastName", author.getLastName()),
            keyHolder,
            new String[]{"id"}
        );

        author.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return author;
    }

    @Override
    public List<Author> findByBook(Book book) {
        return
            jdbc.query(
                "select a.id, a.first_name, a.last_name from author a " +
                "join book_author ba on a.id = ba.author_id " +
                "where ba.book_id = :bookId",
                Map.of("bookId", book.getId()),
                new AuthorMapper()
            );
    }

    @Override
    public void update(Author author) {
        jdbc.update(
            "update author set first_name = :firstName, last_name = :lastName where id = :id",
            Map.of(
                "id", author.getId(),
                "firstName", author.getFirstName(),
                "lastName", author.getLastName()
            )
        );
    }

    @Override
    public void delete(long id) {
        jdbc.update(
            "delete from author where id = :id",
            Map.of("id", id)
        );
    }
}
