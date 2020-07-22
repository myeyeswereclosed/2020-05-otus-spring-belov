package ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.comment;

import org.springframework.stereotype.Service;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.OutputFormatter;

@Service
public class CommentOutputFormatter implements OutputFormatter<Comment> {
    @Override
    public String format(Comment comment) {
        return
            new StringBuilder("id:").append(comment.getId())
                .append(", ").append(comment.getText())
                .append(" (for book '").append(comment.getBook().getTitle()).append("')")
                .toString()
        ;
    }
}
