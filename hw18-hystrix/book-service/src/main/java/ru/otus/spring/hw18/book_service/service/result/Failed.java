package ru.otus.spring.hw18.book_service.service.result;

import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
public class Failed<T> implements ServiceResult<T> {
    private String description = "";

    public Failed(String description) {
        this.description = description;
    }

    @Override
    public Optional<T> value() {
        return Optional.empty();
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public String description() {
        return description;
    }
}
