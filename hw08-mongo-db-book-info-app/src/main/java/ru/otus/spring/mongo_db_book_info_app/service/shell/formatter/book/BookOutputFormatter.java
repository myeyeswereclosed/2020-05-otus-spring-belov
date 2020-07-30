package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.book;

import org.springframework.stereotype.Service;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OutputFormatter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookOutputFormatter implements OutputFormatter<BookInfo> {
    private final static String TABBED_NEW_LINE = "\r\n\t";
    private final static String DOUBLE_TABBED_NEW_LINE = "\r\n\t\t";

    @Override
    public String format(BookInfo bookInfo) {
        var book = bookInfo.getBook();

        var result =
            new StringBuilder("Book: ")
                .append(TABBED_NEW_LINE)
                .append("id: ").append(book.getId()).append(TABBED_NEW_LINE)
                .append("title: ").append(book.getTitle()).append(TABBED_NEW_LINE);

        appendIfExists(result, book.getAuthors(), "authors:", Author::getId, Author::fullName);
        appendIfExists(result, book.getGenres(), "genres:", Genre::getId, Genre::getName);
        appendIfExists(result, bookInfo.getComments(), "comments:", Comment::getId, Comment::getText);

        return result.toString();
    }

    private<T> void appendIfExists(
        StringBuilder result,
        List<T> infoItems,
        String itemsName,
        Function<T, String> toId,
        Function<T, String> asString
    ) {
        if (!infoItems.isEmpty()) {
            result
                .append(itemsName).append(DOUBLE_TABBED_NEW_LINE)
                .append(
                    infoItems
                        .stream()
                        .map(
                            item ->
                                new StringBuilder("id:").append(toId.apply(item))
                                    .append(", ").append(asString.apply(item))
                                    .toString()
                        )
                        .collect(Collectors.joining(DOUBLE_TABBED_NEW_LINE))
                )
                .append(TABBED_NEW_LINE);
        }
    }
}
