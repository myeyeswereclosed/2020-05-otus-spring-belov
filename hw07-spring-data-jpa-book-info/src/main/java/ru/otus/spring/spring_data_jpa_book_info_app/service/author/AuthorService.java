package ru.otus.spring.spring_data_jpa_book_info_app.service.author;

import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

public interface AuthorService {
    ServiceResult<Author> create(Author author);

    ServiceResult<Author> update(Author author);

    ServiceResult<Long> remove(long id);
}
