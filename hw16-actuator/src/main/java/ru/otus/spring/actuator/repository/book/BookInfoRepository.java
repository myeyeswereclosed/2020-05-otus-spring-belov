package ru.otus.spring.actuator.repository.book;

import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.domain.Genre;

public interface BookInfoRepository {
    void updateAuthor(Author author);

    void removeAuthor(String authorId);

    void updateGenre(Genre genre);

    void removeGenre(String genreId);
}
