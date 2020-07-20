package ru.otus.spring.jpa_book_info_app.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.jpa_book_info_app.domain.Author;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.dto.BookAuthor;
import ru.otus.spring.jpa_book_info_app.dto.BookGenre;

import java.util.List;

public interface BookAuthorRepository extends JpaRepository<Author, Long> {
    @Query(
        value =
            "select a.id authorId, a.first_name authorFirstName, a.last_name authorLastName, ba.book_id bookId " +
            "from author a join book_author ba " +
            "on ba.author_id = a.id",
        nativeQuery = true
    )
    List<BookAuthor> findAllWithBooks();
}
