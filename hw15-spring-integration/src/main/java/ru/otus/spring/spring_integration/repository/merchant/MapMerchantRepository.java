package ru.otus.spring.spring_integration.repository.merchant;

import org.springframework.stereotype.Repository;
import ru.otus.spring.spring_integration.domain.Merchant;

import java.net.URI;
import java.util.*;

@Repository
public class MapMerchantRepository implements MerchantRepository {
    private final Map<UUID, Merchant> storage = generate();

    @Override
    public Optional<Merchant> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Merchant> findAll() {
        return new ArrayList<>(storage.values());
    }

    private static Map<UUID, Merchant> generate() {
        var storage = new HashMap<UUID, Merchant>();

        for (int i = 1; i <= 25; i++) {
            var id = UUID.randomUUID();

            storage.put(
                id,
                new Merchant(id, "Merchant_" + i, URI.create("http://localhost:" + i*100))
            );
        }

        return storage;
    }
}
