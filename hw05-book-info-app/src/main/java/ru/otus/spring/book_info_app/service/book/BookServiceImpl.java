package ru.otus.spring.book_info_app.service.book;

import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.book.BookDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.FailResult;
import ru.otus.spring.book_info_app.service.result.ServiceResult;
import ru.otus.spring.book_info_app.service.result.SuccessResult;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookServiceImpl.class);

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public ServiceResult<Book> addBook(String title) {
        try {
            return new SuccessResult<>(bookDao.save(title));
        } catch (Exception e) {
           logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Book> find(long id) {
        try {
            return new SuccessResult<>(bookDao.findById(id));
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Book> rename(long id, String title) {
        try {
            bookDao.update(id, title);

            return new SuccessResult<>(new Book(id, title));
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Void> remove(long id) {
        try {
            bookDao.delete(id);

            return SuccessResult.unit();
        } catch (Exception e) {
            return new FailResult<>();
        }
    }

    @Override
    public ServiceResult<List<Book>> getAll() {
        try {
            return new SuccessResult<>(bookDao.findAll());
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Void> addAuthor(long bookId, Author author) {
        try {
            bookDao.addAuthor(bookId, author);

            return SuccessResult.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Void> addGenre(long bookId, Genre genre) {
        try {
            bookDao.addGenre(bookId, genre);

            return SuccessResult.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }
}
