package ru.otus.spring.actuator.service.genre;

import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.service.result.ServiceResult;

public interface GenreService {
    ServiceResult<Genre> create(Genre genre);
}
