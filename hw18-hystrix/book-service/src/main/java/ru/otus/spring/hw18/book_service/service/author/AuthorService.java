package ru.otus.spring.hw18.book_service.service.author;

import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);
}
