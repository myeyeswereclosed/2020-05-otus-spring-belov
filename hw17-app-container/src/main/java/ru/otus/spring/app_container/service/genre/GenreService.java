package ru.otus.spring.app_container.service.genre;

import ru.otus.spring.app_container.domain.Genre;
import ru.otus.spring.app_container.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);
}
