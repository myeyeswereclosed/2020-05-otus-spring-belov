package ru.otus.spring.app_container.repository.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.app_container.domain.Comment;

import java.util.List;

@RepositoryRestResource
public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {
    @RestResource(path = "comments", rel = "comments")
    List<Comment> findAll();

    List<Comment> findAllByBook_Id(@Param("id") String bookId);

    @RestResource(path = "byBookId")
    List<Comment> findShortByBook_Id(@Param("id") String bookId);

    void deleteAllByBook_Id(String bookId);
}
