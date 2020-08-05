package ru.otus.spring.hw10_rest_book_info_app.controller.rest;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.otus.spring.web_ui_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.web_ui_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

import javax.validation.constraints.NotNull;
import java.util.Optional;
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

    public<T> T handle(ServiceResult<T> serviceResult, T onFailure, T onEmpty) {
        return
            !serviceResult.isOk()
                ? onFailure
                : serviceResult.value().orElse(onEmpty)
            ;
    }
}
