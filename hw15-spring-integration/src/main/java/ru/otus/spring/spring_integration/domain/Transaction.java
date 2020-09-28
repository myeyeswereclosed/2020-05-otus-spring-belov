package ru.otus.spring.spring_integration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Transaction {
    private final UUID id;
    private final int amount;
    private final String currency;
    private final Payment payment;

    public String amountInCurrency() {
        return amount + " " + currency;
    }
}
