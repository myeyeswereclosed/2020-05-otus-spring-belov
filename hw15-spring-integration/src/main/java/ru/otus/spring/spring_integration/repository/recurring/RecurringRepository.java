package ru.otus.spring.spring_integration.repository.recurring;

import ru.otus.spring.spring_integration.domain.Recurring;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecurringRepository {
    Optional<Recurring> findById(UUID id);

    Optional<Recurring> register(Recurring recurring);

    List<Recurring> findAll();
}
