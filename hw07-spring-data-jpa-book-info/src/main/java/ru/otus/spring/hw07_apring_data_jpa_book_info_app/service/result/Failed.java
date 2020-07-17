package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.result;

import java.util.Optional;

public class Failed<T> implements ServiceResult<T> {
    @Override
    public Optional<T> value() {
        return Optional.empty();
    }

    @Override
    public boolean isOk() {
        return false;
    }
}
