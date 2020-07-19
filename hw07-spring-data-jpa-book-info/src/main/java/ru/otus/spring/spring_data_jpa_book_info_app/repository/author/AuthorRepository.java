package ru.otus.spring.spring_data_jpa_book_info_app.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookAuthor;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
//    Author save(Author author);
//
//    boolean delete(long id);
//
//    List<Author> findAll();

    @Query(name = "bookAuthors", nativeQuery = true)
    List<BookAuthor> findAllWithBooks();

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
