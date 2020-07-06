package ru.otus.spring.book_info_app.service.genre;

import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.List;

public interface GenreService {
    ServiceResult<Genre> getByName(String name);

    ServiceResult<Genre> create(String name);

    ServiceResult<Void> update(long id, String name);

    ServiceResult<Void> delete(long id);

    ServiceResult<List<Genre>> getByBook(Book book);
}
