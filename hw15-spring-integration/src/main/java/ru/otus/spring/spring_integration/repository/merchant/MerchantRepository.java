package ru.otus.spring.spring_integration.repository.merchant;

import ru.otus.spring.spring_integration.domain.Merchant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository {
    Optional<Merchant> findById(UUID id);

    List<Merchant> findAll();
}
