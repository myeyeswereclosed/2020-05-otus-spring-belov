package ru.otus.spring.spring_boot_testing_app.service.input;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_boot_testing_app.infrastructure.AppLogger;
import ru.otus.spring.spring_boot_testing_app.infrastructure.AppLoggerFactory;

import java.util.Optional;
import java.util.Scanner;

@Service
public class ConsoleInputReader implements UserInputReader {
    private final static AppLogger logger = AppLoggerFactory.logger(ConsoleInputReader.class);

    private final Scanner scanner = new Scanner(System.in);

    public Optional<String> read() {
        try {
            return Optional.of(scanner.nextLine());
        } catch (Exception e) {
            logger.logException(e);
        }

        return Optional.empty();
    }
}
