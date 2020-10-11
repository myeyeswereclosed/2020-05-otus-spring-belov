package ru.otus.spring.app_container.service.result;

import java.util.Optional;

public interface ServiceResult<T> {
    Optional<T> value();

    boolean isOk();

    String description();
}
