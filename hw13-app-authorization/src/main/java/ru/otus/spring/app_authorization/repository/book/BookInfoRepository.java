package ru.otus.spring.app_authorization.repository.book;

import ru.otus.spring.app_authorization.domain.Author;
import ru.otus.spring.app_authorization.domain.Genre;

public interface BookInfoRepository {
    void updateAuthor(Author author);

    void removeAuthor(String authorId);

    void updateGenre(Genre genre);

    void removeGenre(String genreId);
}
