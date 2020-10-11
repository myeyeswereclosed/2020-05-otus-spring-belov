package ru.otus.spring.app_container.service.author;

import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);
}
