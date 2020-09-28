package ru.otus.spring.spring_integration.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@ToString
@Getter
public class Order {
    private final UUID id;
    private final String externalId;
    private final int amount;
    private final String currency;
    private final Item item;
}
