package ru.otus.spring.spring_data_jpa_book_info_app.service.book;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.book.BookRepository;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Executed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Failed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements BookService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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
            if (bookRepository.updateTitleById(book.getId(), book.getTitle()) > 0) {
                return new Executed<>(book);
            }

            logger.warn("Book with id = {} not found", book.getId());

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<Long> remove(long id) {
        try {
            bookRepository.deleteById(id);

            return new Executed<>(id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Book with id = {} not found", id);

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
