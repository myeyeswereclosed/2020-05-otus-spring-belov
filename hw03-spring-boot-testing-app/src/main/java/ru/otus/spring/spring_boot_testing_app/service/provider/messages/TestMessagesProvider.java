package ru.otus.spring.spring_boot_testing_app.service.provider.messages;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.spring.spring_boot_testing_app.config.Config;
import ru.otus.spring.spring_boot_testing_app.service.provider.BaseProvider;

@Component
public class TestMessagesProvider extends BaseProvider implements MessagesProvider {
    TestMessagesProvider(MessageSource messageSource, Config properties) {
        super(messageSource, properties);
    }

    @Override
    public String provide(String message) {
        return messageSource.getMessage(message, new Object[]{}, locale);
    }

    @Override
    public String provide(String message, String[] data) {
        return messageSource.getMessage(message, data, locale);
    }
}
