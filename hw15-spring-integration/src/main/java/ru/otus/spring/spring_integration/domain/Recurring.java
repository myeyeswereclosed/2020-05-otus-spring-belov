package ru.otus.spring.spring_integration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class Recurring {
    @Setter
    private UUID id;
    private final int amount;
    private final String currency;

    public Recurring(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
