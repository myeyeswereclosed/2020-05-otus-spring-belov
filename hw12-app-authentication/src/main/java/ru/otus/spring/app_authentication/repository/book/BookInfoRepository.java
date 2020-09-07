package ru.otus.spring.app_authentication.repository.book;

import ru.otus.spring.app_authentication.domain.Author;
import ru.otus.spring.app_authentication.domain.Genre;

public interface BookInfoRepository {
    void updateAuthor(Author author);

    void removeAuthor(String authorId);

    void updateGenre(Genre genre);

    void removeGenre(String genreId);
}
