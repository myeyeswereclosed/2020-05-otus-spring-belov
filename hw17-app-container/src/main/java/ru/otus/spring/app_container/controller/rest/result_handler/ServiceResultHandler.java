package ru.otus.spring.app_container.controller.rest.result_handler;

import org.springframework.http.ResponseEntity;
import ru.otus.spring.app_container.service.result.ServiceResult;

import java.util.function.Function;

public interface ServiceResultHandler {
    <T,R> ResponseEntity<R> handle(
        ServiceResult<T> serviceResult,
        Function<T, R> onSuccess,
        ResponseEntity<R> onEmpty,
        ResponseEntity<R> onFailure
    );
}
