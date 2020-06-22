package ru.otus.spring.spring_boot_testing_app.service.provider.files;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.spring.spring_boot_testing_app.config.Config;
import ru.otus.spring.spring_boot_testing_app.service.provider.BaseProvider;

import java.io.File;
import java.util.Optional;

@Component
public class TestFileProvider extends BaseProvider implements FileProvider {
    public TestFileProvider(MessageSource messageSource, Config properties) {
        super(messageSource, properties);
    }

    @Override
    public Optional<File> provide(String pathToFile) {
        try {
            return provideWithoutParams(pathToFile).map(File::new);
        } catch (Exception e) {
            logger.logException(e);
        }

        return Optional.empty();
    }
}
