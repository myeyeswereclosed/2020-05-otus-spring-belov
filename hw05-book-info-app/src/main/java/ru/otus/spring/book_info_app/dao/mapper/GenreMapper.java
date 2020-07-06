package ru.otus.spring.book_info_app.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.book_info_app.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
