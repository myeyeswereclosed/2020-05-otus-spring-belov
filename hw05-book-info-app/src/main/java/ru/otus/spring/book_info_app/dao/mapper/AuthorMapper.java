package ru.otus.spring.book_info_app.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.service.name_parser.NameParser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
    private final NameParser nameParser;

    public AuthorMapper(NameParser nameParser) {
        this.nameParser = nameParser;
    }

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        return
            new Author(
                resultSet.getInt("id"),
                nameParser.parse(resultSet.getString("name"))
            );
    }
}
