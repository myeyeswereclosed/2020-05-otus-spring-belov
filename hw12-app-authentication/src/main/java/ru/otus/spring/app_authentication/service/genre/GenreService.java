package ru.otus.spring.app_authentication.service.genre;

import ru.otus.spring.app_authentication.domain.Genre;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);
}
