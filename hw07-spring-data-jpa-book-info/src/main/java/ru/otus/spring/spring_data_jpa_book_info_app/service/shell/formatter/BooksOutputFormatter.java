package ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksOutputFormatter implements OutputFormatter<List<Book>> {
    private final static String NEW_LINE = "\r\n";

    private final OutputFormatter<Book> bookFormatter;

    public BooksOutputFormatter(OutputFormatter<Book> bookFormatter) {
        this.bookFormatter = bookFormatter;
    }

    @Override
    public String format(List<Book> books) {
        return
            books
                .stream()
                .map(bookFormatter::format)
                .collect(Collectors.joining(NEW_LINE))
        ;
    }
}
