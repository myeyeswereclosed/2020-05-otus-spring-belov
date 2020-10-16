package ru.otus.spring.hw18.book_service.service.book.info.edit;

import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.domain.Genre;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

public interface EditBookInfoService {
    ServiceResult<Author> renameAuthor(Author author);

    ServiceResult<String> removeAuthorById(String id);

    ServiceResult<Genre> renameGenre(Genre genre);

    ServiceResult<String> removeGenreById(String id);
}
