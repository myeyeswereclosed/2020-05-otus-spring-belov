package ru.otus.spring.app_authentication.controller.error_handler;

import org.springframework.validation.BindingResult;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface ErrorHandler {
    Optional<String> handle(BindingResult bindingResult, @NotNull String template);

    <T> Optional<String> handle(ServiceResult<T> serviceResult, @NotNull String template);
}
