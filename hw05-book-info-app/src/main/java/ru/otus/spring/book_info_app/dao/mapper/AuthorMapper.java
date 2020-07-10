package ru.otus.spring.book_info_app.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.book_info_app.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        return
            new Author(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
            );
    }
}
