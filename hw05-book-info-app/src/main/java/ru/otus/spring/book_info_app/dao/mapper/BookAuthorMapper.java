package ru.otus.spring.book_info_app.dao.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.book_info_app.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookAuthorMapper implements RowMapper<Pair<Author, Long>> {
    @Override
    public Pair<Author, Long> mapRow(ResultSet resultSet, int i) throws SQLException {
        return
            Pair.of(
                new Author(
                    resultSet.getInt("author_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name")
                ),
                resultSet.getLong("book_id")
            );
    }
}
