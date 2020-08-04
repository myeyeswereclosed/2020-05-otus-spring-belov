package ru.otus.spring.web_ui_book_info_app.service.book.info.edit;

import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

public interface EditBookInfoService {
    ServiceResult<Author> renameAuthor(Author author);

    ServiceResult<String> removeAuthorById(String id);

    ServiceResult<Genre> renameGenre(Genre genre);

    ServiceResult<String> removeGenreById(String id);
}
