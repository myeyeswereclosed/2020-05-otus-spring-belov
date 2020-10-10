package ru.otus.spring.actuator.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.repository.DuplicatesFinder;
import ru.otus.spring.actuator.repository.author.AuthorRepository;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthorUniquenessHealthIndicator extends UniquenessHealthIndicator<Author> {
    private final AuthorRepository repository;

    @Override
    protected DuplicatesFinder<Author> repository() {
        return repository;
    }

    @Override
    protected Function<Author, String> mapToString() {
        return Author::fullName;
    }
}
