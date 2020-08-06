package ru.otus.spring.rest_book_info_app.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.otus.spring.web_ui_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.web_ui_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ResultHandler {
    private static final AppLogger logger = AppLoggerFactory.logger(ResultHandler.class);

    public Optional<String> handle(BindingResult bindingResult, @NotNull String template) {
        if (bindingResult.hasErrors()) {
            logger.error(
                "BindingErrors occurred:\r\n",
                bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.joining("\r\n"))
            );

            return Optional.of(template);
        }

        return Optional.empty();
    }

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
                    .map(result -> {
                        System.out.println("OLOLO " + result);
                        return new ResponseEntity<>(onSuccess.apply(result), HttpStatus.OK);
                    })
                    .orElse(onEmpty)
            ;
    }
}
