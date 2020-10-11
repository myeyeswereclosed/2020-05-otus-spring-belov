package ru.otus.spring.app_container.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.app_container.domain.Genre;
import ru.otus.spring.app_container.infrastructure.AppLogger;
import ru.otus.spring.app_container.infrastructure.AppLoggerFactory;
import ru.otus.spring.app_container.repository.genre.GenreRepository;
import ru.otus.spring.app_container.service.result.Executed;
import ru.otus.spring.app_container.service.result.Failed;
import ru.otus.spring.app_container.service.result.ServiceResult;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private static final AppLogger logger = AppLoggerFactory.logger(GenreServiceImpl.class);

    private final GenreRepository repository;

    @Override
    @Transactional
    public ServiceResult<Genre> create(Genre genre) {
        try {
            return
                repository
                    .findByName(genre.getName())
                    .<ServiceResult<Genre>>map(authorFound -> new Failed<>("Genre already stored"))
                    .orElse(new Executed<>(repository.save(genre)))
                ;
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
