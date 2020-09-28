package ru.otus.spring.spring_integration.domain;

public enum PaymentType {
    SALE,
    RECURRING;

    public boolean isSale() {
        return this.equals(SALE);
    }

    public boolean isRecurring() {
        return this.equals(RECURRING);
    }
}
