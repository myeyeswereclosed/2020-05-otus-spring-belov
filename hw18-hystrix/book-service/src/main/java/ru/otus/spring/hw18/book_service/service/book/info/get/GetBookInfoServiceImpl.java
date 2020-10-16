package ru.otus.spring.hw18.book_service.service.book.info.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw18.book_service.domain.BookInfo;
import ru.otus.spring.hw18.book_service.infrastructure.AppLogger;
import ru.otus.spring.hw18.book_service.infrastructure.AppLoggerFactory;
import ru.otus.spring.hw18.book_service.repository.book.BookRepository;
import ru.otus.spring.hw18.book_service.service.comment.CommentService;
import ru.otus.spring.hw18.book_service.service.result.Executed;
import ru.otus.spring.hw18.book_service.service.result.Failed;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

@RequiredArgsConstructor
@Service
public class GetBookInfoServiceImpl implements GetBookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(GetBookInfoServiceImpl.class);

    private final BookRepository bookRepository;
    private final CommentService commentService;

    @Override
    public ServiceResult<BookInfo> get(String bookId) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(book -> new Executed<>(new BookInfo(book, commentService.aboutBook(book))))
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
