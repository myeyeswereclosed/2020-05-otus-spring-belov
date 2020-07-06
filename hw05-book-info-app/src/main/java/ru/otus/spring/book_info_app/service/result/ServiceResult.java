package ru.otus.spring.book_info_app.service.result;

import java.util.Optional;

public interface ServiceResult<T> {
    Optional<T> value();

    boolean isOk();
}
