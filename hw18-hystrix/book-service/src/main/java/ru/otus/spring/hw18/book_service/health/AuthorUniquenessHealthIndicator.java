package ru.otus.spring.hw18.book_service.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw18.book_service.domain.Author;
import ru.otus.spring.hw18.book_service.repository.DuplicatesFinder;
import ru.otus.spring.hw18.book_service.repository.author.AuthorRepository;

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
