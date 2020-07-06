package ru.otus.spring.book_info_app.service.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.genre.GenreDao;
import ru.otus.spring.book_info_app.domain.Book;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.FailResult;
import ru.otus.spring.book_info_app.service.result.ServiceResult;
import ru.otus.spring.book_info_app.service.result.SuccessResult;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final static AppLogger logger = AppLoggerFactory.logger(GenreServiceImpl.class);

    private final GenreDao dao;

    public GenreServiceImpl(GenreDao dao) {
        this.dao = dao;
    }

    @Override
    public ServiceResult<Genre> getByName(String name) {
        try {
            return new SuccessResult<>(dao.findByName(name));
        } catch(EmptyResultDataAccessException e) {
            logger.info("Genre not found");
        } catch (Exception e) {
            logger.logException(e);
        }

        return new FailResult<>();
    }

    @Override
    public ServiceResult<Genre> create(String name) {
        try {
            return new SuccessResult<>(dao.save(name));
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
    public ServiceResult<List<Genre>> getByBook(Book book) {
        try {
            return new SuccessResult<>(dao.findByBook(book));
        } catch (Exception e) {
            logger.logException(e);

            return new FailResult<>();
        }
    }
}
