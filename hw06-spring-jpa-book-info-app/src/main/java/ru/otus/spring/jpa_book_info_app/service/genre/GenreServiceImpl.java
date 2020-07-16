package ru.otus.spring.jpa_book_info_app.service.genre;

import org.springframework.stereotype.Service;
import ru.otus.spring.jpa_book_info_app.domain.Genre;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.jpa_book_info_app.repository.genre.GenreRepository;
import ru.otus.spring.jpa_book_info_app.service.result.Executed;
import ru.otus.spring.jpa_book_info_app.service.result.Failed;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import javax.transaction.Transactional;

@Service
public class GenreServiceImpl implements GenreService {
    private final static AppLogger logger = AppLoggerFactory.logger(GenreServiceImpl.class);

    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository dao) {
        this.repository = dao;
    }

    @Override
    @Transactional
    public ServiceResult<Genre> create(Genre genre) {
        try {
            return new Executed<>(repository.save(genre));
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    @Transactional
    public ServiceResult<Void> update(Genre genre) {
        try {
            repository.save(genre);

            return Executed.unit();
        } catch (Exception e) {
            return new Failed<>();
        }
    }

    @Override
    @Transactional
    public ServiceResult<Void> remove(int id) {
        try {
            if (repository.delete(id)) {
                return Executed.unit();
            }

            logger.warn("Genre with id = {} not found", id);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
