package ru.otus.spring.app_container.repository.book;

import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.domain.Genre;

public interface BookInfoRepository {
    void updateAuthor(Author author);

    void removeAuthor(String authorId);

    void updateGenre(Genre genre);

    void removeGenre(String genreId);
}
