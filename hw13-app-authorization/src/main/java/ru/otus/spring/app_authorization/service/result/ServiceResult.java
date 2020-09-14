package ru.otus.spring.app_authorization.service.result;

import java.util.Optional;

public interface ServiceResult<T> {
    Optional<T> value();

    boolean isOk();

    String description();
}
