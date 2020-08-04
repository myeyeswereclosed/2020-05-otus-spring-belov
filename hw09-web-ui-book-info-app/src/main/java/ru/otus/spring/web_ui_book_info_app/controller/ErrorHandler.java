package ru.otus.spring.web_ui_book_info_app.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.otus.spring.web_ui_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.web_ui_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ErrorHandler {
    private static final AppLogger logger = AppLoggerFactory.logger(ErrorHandler.class);

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

    public<T> Optional<String> handle(ServiceResult<T> serviceResult, @NotNull String template) {
        return !serviceResult.isOk() ? Optional.of(template) : Optional.empty();
    }
}
