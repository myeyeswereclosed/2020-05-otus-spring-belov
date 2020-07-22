package ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.comment;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.OutputFormatter;

import java.util.List;

@Service
public class CommentOperationFormatterImpl implements CommentOperationFormatter {
    private final OutputFormatter<Comment> commentOutputFormatter;
    private final OutputFormatter<List<Comment>> commentsOutputFormatter;

    public CommentOperationFormatterImpl(
        OutputFormatter<Comment> commentOutputFormatter,
        OutputFormatter<List<Comment>> commentsOutputFormatter
    ) {
        this.commentOutputFormatter = commentOutputFormatter;
        this.commentsOutputFormatter = commentsOutputFormatter;
    }

    @Override
    public String editInfo(Comment comment) {
        return
            new StringBuilder("Comment edited as '")
                .append(comment.getText())
                .append('\'')
                .toString()
        ;
    }

    @Override
    public String removeInfo(long id) {
        return
            new StringBuilder("Comment with id = ")
                .append(id)
                .append(" removed")
                .toString()
        ;
    }

    @Override
    public String info(Comment comment) {
        return commentOutputFormatter.format(comment);
    }

    @Override
    public String listInfo(List<Comment> comments) {
        return commentsOutputFormatter.format(comments);
    }
}
