package ru.otus.spring.reactive_book_info_app.repository.comment;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String>, CommentRepositoryCustom {
    Flux<Comment> findAllByBook_Id(String bookId);

    Mono<Long> deleteAllByBook_Id(String bookId);
}
