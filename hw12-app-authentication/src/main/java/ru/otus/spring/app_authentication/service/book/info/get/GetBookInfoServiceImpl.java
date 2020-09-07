package ru.otus.spring.app_authentication.service.book.info.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.app_authentication.domain.Comment;
import ru.otus.spring.app_authentication.dto.BookInfo;
import ru.otus.spring.app_authentication.infrastructure.AppLogger;
import ru.otus.spring.app_authentication.infrastructure.AppLoggerFactory;
import ru.otus.spring.app_authentication.repository.book.BookRepository;
import ru.otus.spring.app_authentication.repository.comment.CommentRepository;
import ru.otus.spring.app_authentication.service.result.Executed;
import ru.otus.spring.app_authentication.service.result.Failed;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class GetBookInfoServiceImpl implements GetBookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(GetBookInfoServiceImpl.class);

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<BookInfo> get(String bookId) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(
                        book ->
                            new Executed<>(
                                new BookInfo(
                                    book,
                                    commentRepository.findAllByBook_Id(bookId)
                                )
                            )
                    )
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<List<BookInfo>> getAll() {
        try {
            var comments = comments();

            return
                new Executed<>(
                    bookRepository
                        .findAll()
                        .stream()
                        .map(
                            book ->
                                new BookInfo(
                                    book,
                                    comments.getOrDefault(book.getId(), Collections.emptyList())
                                )
                        )
                        .collect(toList())
                );
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private Map<String, List<Comment>> comments() {
        return
            commentRepository
                .findAll()
                .parallelStream()
                .collect(
                    Collectors.groupingBy(
                        comment -> comment.getBook().getId(),
                        Collectors.toList()
                    )
                )
            ;
    }
}
