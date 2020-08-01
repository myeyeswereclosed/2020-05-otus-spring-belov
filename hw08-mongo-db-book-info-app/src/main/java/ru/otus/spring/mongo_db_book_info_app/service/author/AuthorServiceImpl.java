package ru.otus.spring.mongo_db_book_info_app.service.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.mongo_db_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.mongo_db_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.mongo_db_book_info_app.service.result.Executed;
import ru.otus.spring.mongo_db_book_info_app.service.result.Failed;
import ru.otus.spring.mongo_db_book_info_app.service.result.ServiceResult;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private static final AppLogger logger = AppLoggerFactory.logger(AuthorServiceImpl.class);

    private final AuthorRepository repository;

    @Override
    @Transactional
    public ServiceResult<Author> create(Author author) {
        try {
            return
                repository
                    .findByFirstNameAndLastName(author.getFirstName(), author.getLastName())
                    .<ServiceResult<Author>>map(authorFound -> new Failed<>("Author already stored"))
                    .orElse(new Executed<>(repository.save(author)))
            ;
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
