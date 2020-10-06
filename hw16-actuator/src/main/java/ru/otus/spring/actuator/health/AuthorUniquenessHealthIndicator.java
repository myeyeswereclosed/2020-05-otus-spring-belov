package ru.otus.spring.actuator.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.repository.FindDuplicates;
import ru.otus.spring.actuator.repository.author.AuthorRepository;
import ru.otus.spring.actuator.repository.genre.GenreRepository;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthorUniquenessHealthIndicator extends UniquenessHealthIndicator<Author> {
    private final AuthorRepository repository;

    @Override
    protected FindDuplicates<Author> repository() {
        return repository;
    }

    @Override
    protected Function<Author, String> mapToString() {
        return Author::fullName;
    }
}
