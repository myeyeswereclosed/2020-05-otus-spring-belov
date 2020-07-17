package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.result;

import java.util.Optional;

public interface ServiceResult<T> {
    Optional<T> value();

    boolean isOk();
}
