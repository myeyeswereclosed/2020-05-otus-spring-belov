package ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.author;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.OperationResultFormatter;

@Service
public class AuthorOperationFormatter implements OperationResultFormatter<Author> {
    @Override
    public String editInfo(Author edited) {
        return new StringBuilder("Author renamed to '").append(edited.fullName()).append('\'').toString();
    }

    @Override
    public String removeInfo(long id) {
        return new StringBuilder("Author with id = ").append(id).append(" removed").toString();
    }

    @Override
    public String info(Author author) {
        return new StringBuilder("id: ").append(author.getId()).append(", ").append(author.fullName()).toString();
    }
}
