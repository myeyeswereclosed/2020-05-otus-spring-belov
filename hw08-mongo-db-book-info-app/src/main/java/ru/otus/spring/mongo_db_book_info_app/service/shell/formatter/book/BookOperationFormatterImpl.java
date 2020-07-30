package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.book;

import org.springframework.stereotype.Service;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OutputFormatter;

import java.util.List;

@Service
public class BookOperationFormatterImpl implements BookOperationFormatter {
    private final OutputFormatter<BookInfo> bookOutputFormatter;
    private final OutputFormatter<List<BookInfo>> booksOutputFormatter;

    public BookOperationFormatterImpl(
        OutputFormatter<BookInfo> bookOutputFormatter,
        OutputFormatter<List<BookInfo>> booksOutputFormatter
    ) {
        this.bookOutputFormatter = bookOutputFormatter;
        this.booksOutputFormatter = booksOutputFormatter;
    }

    @Override
    public String editInfo(BookInfo bookInfo) {
        return new StringBuilder("Book renamed to '").append(bookInfo.getBook().getTitle()).append('\'').toString();
    }

    @Override
    public String removeInfo(String id) {
        return new StringBuilder("Book with id = ").append(id).append(" deleted").toString();
    }

    @Override
    public String info(BookInfo bookInfo) {
        return bookOutputFormatter.format(bookInfo);
    }

    @Override
    public String listInfo(List<BookInfo> bookInfoList) {
        return booksOutputFormatter.format(bookInfoList);
    }
}
