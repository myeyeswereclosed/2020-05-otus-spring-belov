package ru.otus.spring.book_info_app.service.author;

import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.List;

public interface AuthorService {
    ServiceResult<Author> getByName(Name name);

    ServiceResult<Author> create(Name name);

    ServiceResult<Void> update(long id, String name);

    ServiceResult<Void> delete(long id);

    ServiceResult<List<Author>> getByBook(Book book);
}
