package ru.otus.spring.actuator.service.author;

import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);
}
