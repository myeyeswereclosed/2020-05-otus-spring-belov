package ru.otus.spring.spring_shell_testing_app.service.provider.files;

import java.io.File;
import java.util.Optional;

public interface FileProvider {
    Optional<File> provide(String pathToFile);
}
