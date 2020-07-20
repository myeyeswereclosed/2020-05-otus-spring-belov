package ru.otus.spring.jpa_book_info_app.repository.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.dto.BookGenre;

import java.util.List;

public interface BookGenreRepository extends JpaRepository<Genre, Long> {
    @Query(
        value =
            "select " +
            "g.id genreId, g.name genreName, bg.book_id bookId " +
            "from genre g join book_genre bg on bg.genre_id = g.id",
        nativeQuery = true
    )
    List<BookGenre> findAllWithBooks();
}
