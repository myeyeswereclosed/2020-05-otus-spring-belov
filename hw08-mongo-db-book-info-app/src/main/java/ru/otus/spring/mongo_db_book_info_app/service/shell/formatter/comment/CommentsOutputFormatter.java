package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.comment;

import org.springframework.stereotype.Service;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OutputFormatter;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsOutputFormatter implements OutputFormatter<List<Comment>> {
    private final static String NEW_LINE = "\r\n";

    private OutputFormatter<Comment> commentFormatter;

    public CommentsOutputFormatter(OutputFormatter<Comment> commentFormatter) {
        this.commentFormatter = commentFormatter;
    }

    @Override
    public String format(List<Comment> comments) {
        return
            comments
                .stream()
                .map(commentFormatter::format)
                .collect(Collectors.joining(NEW_LINE))
            ;
    }
}
