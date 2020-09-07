package ru.otus.spring.app_authentication.service.book.info.edit;

import ru.otus.spring.app_authentication.domain.Author;
import ru.otus.spring.app_authentication.domain.Genre;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

public interface EditBookInfoService {
    ServiceResult<Author> renameAuthor(Author author);

    ServiceResult<String> removeAuthorById(String id);

    ServiceResult<Genre> renameGenre(Genre genre);

    ServiceResult<String> removeGenreById(String id);
}
