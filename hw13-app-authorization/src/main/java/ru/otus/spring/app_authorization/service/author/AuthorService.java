package ru.otus.spring.app_authorization.service.author;

import ru.otus.spring.app_authorization.domain.Author;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);
}
