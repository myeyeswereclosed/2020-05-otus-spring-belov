package ru.otus.spring.hw18.book_service.service.genre;

import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);
}
