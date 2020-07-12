package ru.otus.spring.book_info_app.dao.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.book_info_app.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreAuthorMapper implements RowMapper<Pair<Genre, Long>> {
    @Override
    public Pair<Genre, Long> mapRow(ResultSet resultSet, int i) throws SQLException {
        return
            Pair.of(
                new Genre(
                    resultSet.getInt("genre_id"),
                    resultSet.getString("name")
                ),
                resultSet.getLong("book_id")
            );
    }
}
