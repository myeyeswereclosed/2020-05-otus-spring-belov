package ru.otus.spring.rest_book_info_app.controller.rest.result_handler;

import org.springframework.http.ResponseEntity;
import ru.otus.spring.rest_book_info_app.service.result.ServiceResult;

import java.util.function.Function;

public interface ServiceResultHandler {
    <T,R> ResponseEntity<R> handle(
        ServiceResult<T> serviceResult,
        Function<T, R> onSuccess,
        ResponseEntity<R> onEmpty,
        ResponseEntity<R> onFailure
    );
}
