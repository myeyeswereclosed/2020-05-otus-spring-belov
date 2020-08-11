package ru.otus.spring.rest_book_info_app.service.result;

import java.util.Optional;

public class Executed<T> implements ServiceResult<T> {
    private final T value;
    private String description = "Ok";

    public Executed(T value) {
        this.value = value;
    }

    public static Executed<Void> unit() {
        return new Executed<>(null);
    }

    public static<T> Executed<T> empty() {
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

    @Override
    public String description() {
        return "Ok";
    }
}
