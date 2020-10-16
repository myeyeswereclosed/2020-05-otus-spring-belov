package ru.otus.spring.hw18.book_service.controller.result_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

import java.util.function.Function;

@Component
public class ServiceResultHandlerImpl implements ServiceResultHandler {
    public<T,R> ResponseEntity<R> handle(
        ServiceResult<T> serviceResult,
        Function<T, R> onSuccess,
        ResponseEntity<R> onEmpty,
        ResponseEntity<R> onFailure
    ) {
        return
            !serviceResult.isOk()
                ? onFailure
                : serviceResult
                    .value()
                    .map(result -> new ResponseEntity<>(onSuccess.apply(result), HttpStatus.OK))
                    .orElse(onEmpty)
            ;
    }
}
