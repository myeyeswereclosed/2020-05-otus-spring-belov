package ru.otus.spring.spring_data_jpa_book_info_app.service.genre;

import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);

    ServiceResult<Genre> update(Genre genre);

    ServiceResult<Integer> remove(int id);
}
