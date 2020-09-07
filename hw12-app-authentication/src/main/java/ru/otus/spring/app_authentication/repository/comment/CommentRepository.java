package ru.otus.spring.app_authentication.repository.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.app_authentication.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {
    List<Comment> findAllByBook_Id(String bookId);

    void deleteAllByBook_Id(String bookId);
}
