package ru.otus.spring.book_info_app.service.genre;

import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);

    ServiceResult<Void> update(Genre genre);

    ServiceResult<Void> remove(long id);
}
