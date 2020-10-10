package ru.otus.spring.actuator.service.book.info.edit;

import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.service.result.ServiceResult;

public interface EditBookInfoService {
    ServiceResult<Author> renameAuthor(Author author);

    ServiceResult<String> removeAuthorById(String id);

    ServiceResult<Genre> renameGenre(Genre genre);

    ServiceResult<String> removeGenreById(String id);
}
