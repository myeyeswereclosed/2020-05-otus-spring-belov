package ru.otus.spring.web_ui_book_info_app.repository.book;

import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;

public interface BookInfoRepository {
    void updateAuthor(Author author);

    void removeAuthor(String authorId);

    void updateGenre(Genre genre);

    void removeGenre(String genreId);
}
