package ru.otus.spring.spring_integration.repository.recurring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.spring.spring_integration.domain.Recurring;

import java.util.*;

@Repository
public class MapRecurringRepository implements RecurringRepository {
    private static final Logger logger = LoggerFactory.getLogger(MapRecurringRepository.class);

    private Map<UUID, Recurring> storage = generate();

    @Override
    public Optional<Recurring> findById(UUID id) {
        try {
            return Optional.ofNullable(storage.get(id));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Recurring> register(Recurring recurring) {
        if (Objects.isNull(recurring)) {
            logger.error("Null recurring passed");

            return Optional.empty();
        }

        try {
            storage.put(recurring.getId(), recurring);

            return Optional.of(recurring);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Recurring> findAll() {
        return new ArrayList<>(storage.values());
    }

    private static Map<UUID, Recurring> generate() {
        var storage = new HashMap<UUID, Recurring>();

        for (int i = 1; i <= 10; i++) {
            var id = UUID.randomUUID();

            storage.put(id, new Recurring(id, i*100, "RUB"));
        }

        return storage;
    }
}
