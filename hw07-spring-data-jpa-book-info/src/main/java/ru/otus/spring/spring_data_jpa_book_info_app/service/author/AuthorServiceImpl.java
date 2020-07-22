package ru.otus.spring.spring_data_jpa_book_info_app.service.author;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Executed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Failed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final AppLogger logger = AppLoggerFactory.logger(AuthorServiceImpl.class);

    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ServiceResult<Author> create(Author author) {
        try {
            return new Executed<>(repository.save(author));
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    @Transactional
    public ServiceResult<Author> update(Author author) {
        try {
            if (repository.updateNameById(author.getId(), author.getFirstName(), author.getLastName()) > 0) {
                return new Executed<>(author);
            }

            logger.warn("Author with id = {} not found", author.getId());

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    public ServiceResult<Long> remove(long id) {
        try {
            repository.deleteById(id);

            return new Executed<>(id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Author with id = {} not found", id);

            return Executed.empty();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
