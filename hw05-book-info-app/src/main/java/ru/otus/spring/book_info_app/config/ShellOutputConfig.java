package ru.otus.spring.book_info_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="shell.output")
public class ShellOutputConfig {
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;


}
