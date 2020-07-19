package ru.otus.spring.spring_data_jpa_book_info_app.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query("update Comment c set c.text = :text where c.id = :id")
    int updateTextById(@Param("id") long id, @Param("text") String text);
//
//    Optional<Comment> findById(long id);
//
//    List<Comment> findAll();
//
//    boolean delete(long id);
}
