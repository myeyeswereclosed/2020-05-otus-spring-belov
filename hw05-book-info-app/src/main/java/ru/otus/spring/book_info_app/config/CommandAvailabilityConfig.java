package ru.otus.spring.book_info_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="shell.availability")
public class CommandAvailabilityConfig {
    private String noCurrentBookMessage;
    private String sessionNotFinishedMessage;

    public String getNoCurrentBookMessage() {
        return noCurrentBookMessage;
    }

    public void setNoCurrentBookMessage(String noCurrentBookMessage) {
        this.noCurrentBookMessage = noCurrentBookMessage;
    }

    public String getSessionNotFinishedMessage() {
        return sessionNotFinishedMessage;
    }

    public void setSessionNotFinishedMessage(String sessionNotFinishedMessage) {
        this.sessionNotFinishedMessage = sessionNotFinishedMessage;
    }
}
