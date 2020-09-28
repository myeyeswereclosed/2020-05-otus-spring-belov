package ru.otus.spring.spring_integration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.spring_integration.domain.order.Order;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Payment {
    private final Order order;
    private final Merchant merchant;
    private final Customer customer;
    private final PaymentType paymentType;
    private final UUID recurringId;

    public static Payment error() {
        return new Payment(null, null, null, null, null);
    }

    public boolean isError() {
        return Objects.isNull(merchant);
    }

    public boolean isSale() {
        return paymentType.isSale();
    }

    public boolean isRecurring() {
        return paymentType.isRecurring();
    }
}
