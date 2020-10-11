package ru.otus.spring.app_container.service.book.info.edit;

import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.domain.Genre;
import ru.otus.spring.app_container.service.result.ServiceResult;

public interface EditBookInfoService {
    ServiceResult<Author> renameAuthor(Author author);

    ServiceResult<String> removeAuthorById(String id);

    ServiceResult<Genre> renameGenre(Genre genre);

    ServiceResult<String> removeGenreById(String id);
}
