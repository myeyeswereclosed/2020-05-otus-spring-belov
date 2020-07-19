package ru.otus.spring.spring_data_jpa_book_info_app.service.book;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.book.BookRepository;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Executed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Failed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

import javax.transaction.Transactional;

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
    public ServiceResult<Void> rename(Book book) {
        try {
            bookRepository.updateTitleById(book.getId(), book.getTitle());

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<Void> remove(long id) {
        try {
            bookRepository.deleteById(id);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
