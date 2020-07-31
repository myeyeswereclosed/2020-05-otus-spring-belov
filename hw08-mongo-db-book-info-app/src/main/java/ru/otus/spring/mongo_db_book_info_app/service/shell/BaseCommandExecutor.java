package ru.otus.spring.mongo_db_book_info_app.service.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import ru.otus.spring.mongo_db_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.mongo_db_book_info_app.service.result.ServiceResult;

import java.util.function.Function;

@ShellComponent
@RequiredArgsConstructor
public class BaseCommandExecutor {
    private final ShellOutputConfig config;

    protected <T> String output(ServiceResult<T> serviceResult, Function<T, String> onValue) {
        return
            serviceResult.isOk()
                ? serviceResult.value().map(onValue).orElse(config.getNotFoundMessage())
                : errorMessage(serviceResult)
            ;
    }

    protected String output(ServiceResult<Void> serviceResult, String message) {
        return serviceResult.isOk() ? message : errorMessage(serviceResult);
    }

    private <T> String errorMessage(ServiceResult<T> result) {
        var description = result.description();

        return description.isEmpty() ? config.getErrorMessage() : description;
    }

    protected String output(ServiceResult<Void> serviceResult) {
        return serviceResult.isOk() ? config.getDefaultMessage() : config.getErrorMessage();
    }
}
