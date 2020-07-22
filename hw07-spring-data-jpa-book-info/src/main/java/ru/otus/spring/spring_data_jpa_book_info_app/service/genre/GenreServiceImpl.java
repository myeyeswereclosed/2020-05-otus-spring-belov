package ru.otus.spring.spring_data_jpa_book_info_app.service.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Genre;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.genre.GenreRepository;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Executed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Failed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;
import org.springframework.transaction.annotation.Transactional;

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
    public ServiceResult<Genre> update(Genre genre) {
        try {
            if (repository.updateNameById(genre.getId(), genre.getName()) > 0) {
                return new Executed<>(genre);
            }

            logger.warn("Genre with id = {} not found", genre.getId());

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    public ServiceResult<Integer> remove(int id) {
        try {
            repository.deleteById(id);

            return new Executed<>(id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Genre with id = {} not found", id);

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}