package ru.otus.spring.mongo_db_book_info_app.service.genre;

import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);
}
