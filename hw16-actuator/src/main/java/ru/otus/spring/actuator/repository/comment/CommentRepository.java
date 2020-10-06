package ru.otus.spring.actuator.repository.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.actuator.domain.Comment;

import java.util.List;

@RepositoryRestResource
public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {
    @RestResource(path = "comments", rel = "comments")
    List<Comment> findAll();

    @RestResource(path = "byBookId")
    List<Comment> findAllByBook_Id(@Param("id") String bookId);

    void deleteAllByBook_Id(String bookId);
}
