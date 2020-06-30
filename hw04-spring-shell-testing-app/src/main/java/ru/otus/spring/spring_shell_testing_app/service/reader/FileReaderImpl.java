package ru.otus.spring.spring_shell_testing_app.service.reader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.otus.spring.spring_shell_testing_app.infrastructure.AppLogger;
import ru.otus.spring.spring_shell_testing_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.spring_shell_testing_app.service.provider.files.FileProvider;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class FileReaderImpl implements FileReader {
    private final static String PATH_TO_FILE_KEY="test.file";

    private final static AppLogger logger = AppLoggerFactory.logger(FileReaderImpl.class);

    private final FileProvider provider;

    public FileReaderImpl(FileProvider provider) {
        this.provider = provider;
    }

    public List<String> read() {
        return
            provider
                .provide(PATH_TO_FILE_KEY)
                .map(
                    file -> {
                        try (
                            var inputStream = new ClassPathResource(file.getName()).getInputStream();
                            var inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                            var reader = new BufferedReader(inputReader);
                        ) {
                            List<String> contents = new ArrayList<>();

                            for (String line; (line = reader.readLine()) != null; ) {
                                contents.add(line);
                            }

                            return contents;
                        } catch (Exception e) {
                            logger.logException(e);

                            return new ArrayList<String>();
                        }
                    }
                )
                .orElse(emptyList());
    }
}
