package ru.otus.spring.book_info_app.service.author;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.author.AuthorDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.FailResult;
import ru.otus.spring.book_info_app.service.result.ServiceResult;
import ru.otus.spring.book_info_app.service.result.SuccessResult;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final AppLogger logger = AppLoggerFactory.logger(AuthorServiceImpl.class);

    private final AuthorDao dao;

    public AuthorServiceImpl(AuthorDao dao) {
        this.dao = dao;
    }

    @Override
    public ServiceResult<Author> getByName(Name name) {
        try {
            return new SuccessResult<>(dao.findByName(name.toString()));
        } catch(EmptyResultDataAccessException e) {
            logger.info("Author not found");
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Author> create(Name name) {
        try {
            return new SuccessResult<>(dao.save(name.toString()));
        } catch (Exception e) {
            logger.logException(e);

            return new FailResult<>();
        }
    }

    @Override
    public ServiceResult<Void> update(long id, String name) {
        try {
            dao.update(id, name);

            return SuccessResult.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new FailResult<>();
        }
    }

    @Override
    public ServiceResult<Void> delete(long id) {
        try {
            dao.delete(id);

            return SuccessResult.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new FailResult<>();
        }
    }

    @Override
    public ServiceResult<List<Author>> getByBook(Book book) {
        try {
            return new SuccessResult<>(dao.findByBook(book));
        } catch (Exception e) {
            logger.logException(e);

            return new FailResult<>();
        }
    }
}
