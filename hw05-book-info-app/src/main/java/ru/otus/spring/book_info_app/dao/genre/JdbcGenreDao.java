package ru.otus.spring.book_info_app.dao.genre;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.book_info_app.dao.mapper.GenreMapper;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonMap;

@Repository
public class JdbcGenreDao implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public JdbcGenreDao(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public Genre save(String name) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(
            "insert into genre(name) values(:name)",
            new MapSqlParameterSource().addValue("name", name),
            keyHolder,
            new String[]{"id"}
        );

        return new Genre(Objects.requireNonNull(keyHolder.getKey()).intValue(), name);
    }

    @Override
    public void update(long id, String name) {
        jdbc.update(
            "update genre set name = :name where id = :id",
            Map.of("name", name, "id", id)
        );
    }

    @Override
    public void delete(long id) {
        jdbc.update(
            "delete from genre where id = :id",
            Map.of("id", id)
        );
    }

    @Override
    public Genre findByName(String name) {
        return
            jdbc.queryForObject(
                "select id, name from genre where name = :name",
                Map.of("name", name),
                new GenreMapper()
            );
    }

    @Override
    public List<Genre> findByNames(List<String> names) {
        return jdbc.query(
            "select id, name from genre where name in (:names)",
            singletonMap("names", names),
            new GenreMapper()
        );
    }

    @Override
    public List<Genre> findByBook(Book book) {
        return
            jdbc.query(
                "select g.id, g.name from genre g " +
                "join book_genre bg on g.id = bg.genre_id " +
                "where bg.book_id = :bookId",
                Map.of("bookId", book.getId()),
                new GenreMapper()
            );
    }
}
