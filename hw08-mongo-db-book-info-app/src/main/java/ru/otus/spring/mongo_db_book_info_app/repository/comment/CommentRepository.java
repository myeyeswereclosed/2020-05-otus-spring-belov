package ru.otus.spring.mongo_db_book_info_app.repository.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {
    List<Comment> findAllByBook_Id(String bookId);
}
