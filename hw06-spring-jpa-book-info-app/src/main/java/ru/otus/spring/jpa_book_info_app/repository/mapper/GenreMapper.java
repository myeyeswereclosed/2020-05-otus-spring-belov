package ru.otus.spring.jpa_book_info_app.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.jpa_book_info_app.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
