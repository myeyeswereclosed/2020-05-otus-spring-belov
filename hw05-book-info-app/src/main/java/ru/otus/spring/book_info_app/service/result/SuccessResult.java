package ru.otus.spring.book_info_app.service.result;

import java.util.Optional;

public class SuccessResult<T> implements ServiceResult<T> {
    private final T value;

    public SuccessResult(T value) {
        this.value = value;
    }

    public static SuccessResult<Void> unit() {
        return new SuccessResult<>(null);
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
