package ru.otus.spring.students_testing.service.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReaderImpl implements FileReader {
    private final String fileName;

    public FileReaderImpl(@Value("${test.file}") String fileName) {
        this.fileName = fileName;
    }

    public List<String> read() {
        try (
            var inputStream = new DataInputStream(new FileInputStream(new File(fileName)));
            var inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            var reader = new BufferedReader(inputReader);
        ) {
            List<String> contents = new ArrayList<>();

            for (String line; (line = reader.readLine()) != null; ) {
                contents.add(line);
            }

            return contents;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
