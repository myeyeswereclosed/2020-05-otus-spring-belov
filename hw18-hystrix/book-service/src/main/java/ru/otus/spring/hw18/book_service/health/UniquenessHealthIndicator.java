package ru.otus.spring.hw18.book_service.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import ru.otus.spring.hw18.book_service.repository.DuplicatesFinder;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;

// assume we forgot about unique indexes :)
abstract public class UniquenessHealthIndicator<T> implements HealthIndicator {
    abstract protected DuplicatesFinder<T> repository();

    abstract protected Function<T, String> mapToString();

    @Override
    public Health health() {
        var duplicates = repository().findDuplicates();

        return
            duplicates.isEmpty()
                ? Health.up().withDetail("message", "No duplicates").build()
                : Health
                    .down()
                    .withDetail("duplicates found are:", duplicates.stream().map(mapToString()).collect(toList()))
                    .build();
    }
}
