package ru.otus.spring.mongo_db_book_info_app.service.result;

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
