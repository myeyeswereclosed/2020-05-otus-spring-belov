package ru.otus.spring.spring_data_jpa_book_info_app.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookAuthor;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Modifying
    @Query("update Author a set a.firstName = :firstName, a.lastName = :lastName where a.id = :id")
    int updateNameById(
        @Param("id") long id,
        @Param("firstName") String firstName,
        @Param("lastName") String lastName
    );

    @Query(
        value =
            "select a.id authorId, a.first_name authorFirstName, a.last_name authorLastName, ba.book_id bookId " +
            "from author a join book_author ba " +
            "on ba.author_id = a.id",
        nativeQuery = true
    )
    List<BookAuthor> findAllWithBooks();

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
