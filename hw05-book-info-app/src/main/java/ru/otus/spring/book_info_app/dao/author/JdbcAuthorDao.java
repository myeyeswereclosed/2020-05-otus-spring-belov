package ru.otus.spring.book_info_app.dao.author;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.book_info_app.dao.mapper.AuthorMapper;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.service.name_parser.NameParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

@Repository
public class JdbcAuthorDao implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;
    private final NameParser parser;

    public JdbcAuthorDao(NamedParameterJdbcOperations namedParameterJdbcOperations, NameParser parser) {
        this.jdbc = namedParameterJdbcOperations;
        this.parser = parser;
    }

    @Override
    public List<Author> findByNames(List<String> names) {
        return
            jdbc.query(
            "select id, name from author where name in (:names)",
                singletonMap("names", names),
                new AuthorMapper(parser)
            );
    }

    @Override
    public Author findByName(String name) {
        return
            jdbc.queryForObject(
                "select id, name from author where name = :name",
                Map.of("name", name),
                new AuthorMapper(parser)
            );
    }

    public Author save(String name) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(
            "insert into author(name) values(:name)",
            new MapSqlParameterSource().addValue("name", name),
            keyHolder,
            new String[]{"id"}
        );

        return
            new Author(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                parser.parse(name)
            );
    }

    @Override
    public void save(List<String> names) {
        Map<String, Long>[] maps = new Map[names.size()];

        jdbc.batchUpdate(
            "insert into author(name) values(:name)",
            names
                .stream()
                .map(name -> Map.of("name", name))
                .collect(Collectors.toList())
                .toArray(maps)
        );
    }

    @Override
    public List<Author> findByBook(Book book) {
        return
            jdbc.query(
                "select a.id, a.name from author a " +
                "join book_author ba on a.id = ba.author_id " +
                "where ba.book_id = :bookId",
                Map.of("bookId", book.getId()),
                new AuthorMapper(parser)
            );
    }

    @Override
    public void update(long id, String name) {
        jdbc.update(
            "update author set name = :name where id = :id",
            Map.of("name", name, "id", id)
        );
    }

    @Override
    public Author findById(long id) {
        return
            jdbc
                .queryForObject(
                    "select id, name from author where id = :id",
                    singletonMap("id", id),
                    new AuthorMapper(parser)
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
