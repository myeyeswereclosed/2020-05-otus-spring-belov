package ru.otus.spring.spring_boot_testing_app.service.provider;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import ru.otus.spring.spring_boot_testing_app.config.Config;
import ru.otus.spring.spring_boot_testing_app.infrastructure.AppLogger;
import ru.otus.spring.spring_boot_testing_app.infrastructure.AppLoggerFactory;

import java.util.Locale;
import java.util.Optional;

public class BaseProvider {
    protected static final AppLogger logger = AppLoggerFactory.logger(BaseProvider.class);

    protected final MessageSource messageSource;
    protected final Locale locale;

    public BaseProvider(MessageSource messageSource, Config config) {
        this.messageSource = messageSource;
        this.locale =
            Optional.ofNullable(config.getLocale()).orElseGet(
                () -> {
                    logger.error("Locale not found. Default locale will be used");

                    return Locale.ENGLISH;
                }
            );
    }

    protected Optional<String> provideWithoutParams(String key) {
        try {
            return Optional.of(messageSource.getMessage(key, new Object[]{}, locale));
        } catch (NoSuchMessageException e) {
            logger.getLogger().error("Key '{}' not found for {} locale", key, locale.getLanguage());
        }

        return Optional.empty();
    }
}
