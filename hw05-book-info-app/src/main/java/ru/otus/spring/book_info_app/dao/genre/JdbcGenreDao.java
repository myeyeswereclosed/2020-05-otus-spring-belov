package ru.otus.spring.book_info_app.dao.genre;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.book_info_app.dao.mapper.BookAuthorMapper;
import ru.otus.spring.book_info_app.dao.mapper.GenreAuthorMapper;
import ru.otus.spring.book_info_app.dao.mapper.GenreMapper;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class JdbcGenreDao implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public JdbcGenreDao(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public Genre save(Genre genre) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.update(
            "insert into genre(name) values(:name)",
            new MapSqlParameterSource().addValue("name", genre.getName()),
            keyHolder,
            new String[]{"id"}
        );

        genre.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return genre;
    }

    @Override
    public void update(Genre genre) {
        jdbc.update(
            "update genre set name = :name where id = :id",
            Map.of("name", genre.getName(), "id", genre.getId())
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

    @Override
    public List<Pair<Genre, Long>> findAllWithBooks() {
        return
            jdbc.query(
                "select g.id genre_id, g.name, bg.book_id book_id " +
                "from genre g join book_genre bg " +
                "on bg.genre_id = g.id",
                new GenreAuthorMapper()
            );
    }
}
