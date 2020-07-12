package ru.otus.spring.book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import ru.otus.spring.book_info_app.config.ShellOutputConfig;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.function.Function;

@ShellComponent
public class BaseCommandHandler {
    private final ShellOutputConfig config;

    public BaseCommandHandler(ShellOutputConfig config) {
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
        return output(serviceResult, config.getDefaultMessage());
    }
}
