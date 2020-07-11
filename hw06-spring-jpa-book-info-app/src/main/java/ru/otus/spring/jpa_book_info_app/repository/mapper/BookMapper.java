package ru.otus.spring.jpa_book_info_app.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.spring.jpa_book_info_app.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Book(resultSet.getLong("id"), resultSet.getString("title"));
    }
}
