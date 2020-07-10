package ru.otus.spring.book_info_app.service.result;

import java.util.Optional;

public class Executed<T> implements ServiceResult<T> {
    private final T value;

    public Executed(T value) {
        this.value = value;
    }

    public static Executed<Void> unit() {
        return new Executed<>(null);
    }

    @Override
    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean isOk() {
        return true;
    }
}
