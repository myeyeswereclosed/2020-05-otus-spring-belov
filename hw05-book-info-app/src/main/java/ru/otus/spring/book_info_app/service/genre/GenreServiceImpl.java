package ru.otus.spring.book_info_app.service.genre;

import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.genre.GenreDao;
import ru.otus.spring.book_info_app.domain.Genre;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.Executed;
import ru.otus.spring.book_info_app.service.result.Failed;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

@Service
public class GenreServiceImpl implements GenreService {
    private final static AppLogger logger = AppLoggerFactory.logger(GenreServiceImpl.class);

    private final GenreDao dao;

    public GenreServiceImpl(GenreDao dao) {
        this.dao = dao;
    }

    @Override
    public ServiceResult<Genre> create(Genre genre) {
        try {
            return new Executed<>(dao.save(genre));
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    public ServiceResult<Void> update(Genre genre) {
        try {
            dao.update(genre);

            return Executed.unit();
        } catch (Exception e) {
            return new Failed<>();
        }
    }

    @Override
    public ServiceResult<Void> remove(long id) {
        try {
            dao.delete(id);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
