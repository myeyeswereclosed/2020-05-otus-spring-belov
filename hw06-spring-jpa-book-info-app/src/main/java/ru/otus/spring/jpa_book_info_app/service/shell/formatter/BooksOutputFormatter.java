package ru.otus.spring.jpa_book_info_app.service.shell.formatter;

import org.springframework.stereotype.Service;
import ru.otus.spring.jpa_book_info_app.dto.BookInfo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksOutputFormatter implements OutputFormatter<List<BookInfo>> {
    private final static String NEW_LINE = "\r\n";

    private final OutputFormatter<BookInfo> bookFormatter;

    public BooksOutputFormatter(OutputFormatter<BookInfo> bookFormatter) {
        this.bookFormatter = bookFormatter;
    }

    @Override
    public String format(List<BookInfo> books) {
        return
            books
                .stream()
                .map(bookFormatter::format)
                .collect(Collectors.joining(NEW_LINE))
        ;
    }
}
