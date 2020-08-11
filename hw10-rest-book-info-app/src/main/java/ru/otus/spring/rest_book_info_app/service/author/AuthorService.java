package ru.otus.spring.rest_book_info_app.service.author;

import ru.otus.spring.rest_book_info_app.domain.Author;
import ru.otus.spring.rest_book_info_app.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);
}
