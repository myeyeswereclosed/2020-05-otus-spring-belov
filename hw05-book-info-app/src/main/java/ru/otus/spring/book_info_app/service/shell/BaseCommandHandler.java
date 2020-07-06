package ru.otus.spring.book_info_app.service.shell;

import org.springframework.shell.standard.ShellComponent;
import ru.otus.spring.book_info_app.config.ShellOutputConfig;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

import java.util.function.Function;

@ShellComponent
public class BaseCommandHandler {
    protected final static String NAME_PATTERN = "^[A-Za-z]+(\\s[A-Za-z]+)+";

    private final ShellOutputConfig config;

    public BaseCommandHandler(ShellOutputConfig config) {
        this.config = config;
    }

    protected  <T> String output(ServiceResult<T> serviceResult, Function<T, String> onValue) {
        return
            serviceResult.isOk()
                ? serviceResult.value().map(onValue).orElse("Ok")
                : config.getError()
            ;
    }

    protected String output(ServiceResult<Void> serviceResult, String message) {
        return serviceResult.isOk() ? message : config.getError();
    }

    protected String output(ServiceResult<Void> serviceResult) {
        return serviceResult.isOk() ? "Ok" : config.getError();
    }
}
