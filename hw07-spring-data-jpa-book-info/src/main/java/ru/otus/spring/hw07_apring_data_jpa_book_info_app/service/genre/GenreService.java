package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.genre;

import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);

    ServiceResult<Void> update(Genre genre);

    ServiceResult<Void> remove(int id);
}
