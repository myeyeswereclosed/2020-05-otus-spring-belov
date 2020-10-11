package ru.otus.spring.app_container.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.app_container.domain.Author;
import ru.otus.spring.app_container.repository.DuplicatesFinder;
import ru.otus.spring.app_container.repository.author.AuthorRepository;

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
