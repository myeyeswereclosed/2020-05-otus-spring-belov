package ru.otus.spring.hw18.book_service.repository.book;

import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.domain.Genre;

public interface BookInfoRepository {
    void updateAuthor(Author author);

    void removeAuthor(String authorId);

    void updateGenre(Genre genre);

    void removeGenre(String genreId);
}
