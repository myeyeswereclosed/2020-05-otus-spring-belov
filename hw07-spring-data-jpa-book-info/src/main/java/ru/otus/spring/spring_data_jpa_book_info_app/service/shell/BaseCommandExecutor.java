package ru.otus.spring.spring_data_jpa_book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import ru.otus.spring.spring_data_jpa_book_info_app.config.ShellOutputConfig;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

import java.util.function.Function;

@ShellComponent
public class BaseCommandExecutor {
    private final ShellOutputConfig config;

    public BaseCommandExecutor(ShellOutputConfig config) {
        this.config = config;
    }

    protected  <T> String output(ServiceResult<T> serviceResult, Function<T, String> onValue) {
        return
            serviceResult.isOk()
                ? serviceResult.value().map(onValue).orElse(config.getNotFoundMessage())
                : config.getErrorMessage()
            ;
    }

    protected String output(ServiceResult<Void> serviceResult, String message) {
        return serviceResult.isOk() ? message : config.getErrorMessage();
    }

    protected String output(ServiceResult<Void> serviceResult) {
        return serviceResult.isOk() ? config.getDefaultMessage() : config.getErrorMessage();
    }
}
