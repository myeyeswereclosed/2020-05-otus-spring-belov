package ru.otus.spring.actuator.service.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.infrastructure.AppLogger;
import ru.otus.spring.actuator.infrastructure.AppLoggerFactory;
import ru.otus.spring.actuator.repository.author.AuthorRepository;
import ru.otus.spring.actuator.service.result.Executed;
import ru.otus.spring.actuator.service.result.Failed;
import ru.otus.spring.actuator.service.result.ServiceResult;

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