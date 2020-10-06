package ru.otus.spring.actuator.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.actuator.domain.Book;
import ru.otus.spring.actuator.infrastructure.AppLogger;
import ru.otus.spring.actuator.infrastructure.AppLoggerFactory;
import ru.otus.spring.actuator.repository.book.BookRepository;
import ru.otus.spring.actuator.repository.comment.CommentRepository;
import ru.otus.spring.actuator.repository.comment.UpdateCommentConfig;
import ru.otus.spring.actuator.service.result.Executed;
import ru.otus.spring.actuator.service.result.Failed;
import ru.otus.spring.actuator.service.result.ServiceResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ServiceResult<Book> addBook(Book book) {
        try {
            return new Executed<>(bookRepository.save(book));
        } catch (Exception e) {
           logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<Book> rename(Book book) {
        try {
            return
                bookRepository
                    .update(book)
                    .map(
                        updated -> {
                            commentRepository.updateWithConfig(UpdateCommentConfig.renameBook(book));

                            return new Executed<>(book);
                        }
                    )
                    .orElseGet(
                        () -> {
                            logger.warn("Book with id = {} not found", book.getId());

                            return Executed.empty();
                        }
                    );
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<String> remove(String id) {
        try {
            return
                bookRepository
                    .delete(id)
                    .map(
                        removed -> {
                            commentRepository.deleteAllByBook_Id(id);

                            return new Executed<>(id);
                        }
                    )
                    .orElseGet(
                        () -> {
                            logger.warn("Book with id = {} not found", id);

                            return Executed.empty();
                        }
                    )
            ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<List<Book>> getAll() {
        try {
            return new Executed<>(bookRepository.findAll());
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
