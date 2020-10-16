package ru.otus.spring.hw18.book_service.service.comment;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw18.book_service.domain.Book;
import ru.otus.spring.hw18.book_service.domain.Comment;
import ru.otus.spring.hw18.book_service.infrastructure.AppLogger;
import ru.otus.spring.hw18.book_service.infrastructure.AppLoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BookCommentService implements CommentService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookCommentService.class);

    private final CommentServiceClient client;

    @Override
    @HystrixCommand(fallbackMethod="defaultComments")
    public List<Comment> aboutBook(Book book) {
        logger.info("Trying to get comments using comment service client");

        return
            client
                .allByBookId(book.getId())
                .stream()
                .map(dto -> Comment.fromDto(dto, book))
                .collect(toList())
            ;
    }

    @Override
    public Comment add(Comment comment) {
        logger.info("Trying to add comment {} to {}", comment.getText(), comment.getBook());

        return Comment.fromDto(client.add(comment.toDto()), comment.getBook());
    }

    private List<Comment> defaultComments(Book book) {
        return List.of(Comment.fromText("Comments are unavailable at the moment"));
    }
}
