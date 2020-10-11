package ru.otus.spring.app_container.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.app_container.domain.Genre;
import ru.otus.spring.app_container.repository.DuplicatesFinder;
import ru.otus.spring.app_container.repository.genre.GenreRepository;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class GenreUniquenessHealthIndicator extends UniquenessHealthIndicator<Genre> {
    private final GenreRepository repository;

    @Override
    protected DuplicatesFinder<Genre> repository() {
        return repository;
    }

    @Override
    protected Function<Genre, String> mapToString() {
        return Genre::getName;
    }
}
