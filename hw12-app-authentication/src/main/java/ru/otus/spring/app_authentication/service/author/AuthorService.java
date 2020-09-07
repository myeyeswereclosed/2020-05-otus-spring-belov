package ru.otus.spring.app_authentication.service.author;

import ru.otus.spring.app_authentication.domain.Author;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);
}
