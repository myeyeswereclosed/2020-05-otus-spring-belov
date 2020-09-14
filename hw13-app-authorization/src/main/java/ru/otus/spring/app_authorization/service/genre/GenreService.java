package ru.otus.spring.app_authorization.service.genre;

import ru.otus.spring.app_authorization.domain.Genre;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);
}
