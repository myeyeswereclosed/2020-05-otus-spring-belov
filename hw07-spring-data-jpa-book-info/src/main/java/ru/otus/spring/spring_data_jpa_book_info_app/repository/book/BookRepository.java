package ru.otus.spring.spring_data_jpa_book_info_app.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Query("update Book b set b.title = :title where b.id = :id")
    void updateTitleById(@Param("id") long id, @Param("title") String title);
}
