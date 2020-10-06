package ru.otus.spring.actuator.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.repository.FindDuplicates;
import ru.otus.spring.actuator.repository.genre.GenreRepository;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class GenreUniquenessHealthIndicator extends UniquenessHealthIndicator<Genre> {
    private final GenreRepository repository;

    @Override
    protected FindDuplicates<Genre> repository() {
        return repository;
    }

    @Override
    protected Function<Genre, String> mapToString() {
        return Genre::getName;
    }
}
