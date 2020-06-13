package ru.otus.spring.students_testing.service.input;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class ConsoleInputReader implements UserInputReader {
    private final Scanner scanner = new Scanner(System.in);

    public Optional<String> read() {
        try {
            return Optional.of(scanner.nextLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
