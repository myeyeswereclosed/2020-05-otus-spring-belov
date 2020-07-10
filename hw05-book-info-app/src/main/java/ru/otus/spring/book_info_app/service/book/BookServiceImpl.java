package ru.otus.spring.book_info_app.service.book;

import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.book.BookDao;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.Executed;
import ru.otus.spring.book_info_app.service.result.Failed;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

@Service
public class BookServiceImpl implements BookService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookServiceImpl.class);

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public ServiceResult<Book> addBook(Book book) {
        try {
            return new Executed<>(bookDao.save(book));
        } catch (Exception e) {
           logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<Void> rename(Book book) {
        try {
            bookDao.update(book);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<Void> remove(long id) {
        try {
            bookDao.delete(id);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
