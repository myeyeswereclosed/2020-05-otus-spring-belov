package ru.otus.spring.spring_integration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.spring_integration.domain.order.Item;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class PaymentRequest {
    private final String externalId;
    private final Item item;
    private final int amount;
    private final String currency;
    private final UUID merchantId;
    private final String customerFirstName;
    private final String customerLastName;
    private final String customerEmail;
    private final UUID recurringId;
}
