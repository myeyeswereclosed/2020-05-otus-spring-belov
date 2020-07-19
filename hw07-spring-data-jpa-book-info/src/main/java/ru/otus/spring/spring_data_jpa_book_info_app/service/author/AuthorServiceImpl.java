package ru.otus.spring.spring_data_jpa_book_info_app.service.author;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Author;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.spring_data_jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Executed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.Failed;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

import javax.transaction.Transactional;

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
    public ServiceResult<Void> update(Author author) {
        try {
            repository.save(author);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    @Transactional
    public ServiceResult<Void> remove(long id) {
        try {
            repository.deleteById(id);
                return Executed.unit();

//            logger.warn("Author with id = {} not found", id);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
