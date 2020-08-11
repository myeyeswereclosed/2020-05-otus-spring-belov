package ru.otus.spring.rest_book_info_app.service.result;

import java.util.Optional;

public interface ServiceResult<T> {
    Optional<T> value();

    boolean isOk();

    String description();
}
