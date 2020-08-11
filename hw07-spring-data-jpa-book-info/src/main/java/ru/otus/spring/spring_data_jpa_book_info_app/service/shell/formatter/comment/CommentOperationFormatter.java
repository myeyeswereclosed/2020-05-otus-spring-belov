package ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.comment;

import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.shell.formatter.OperationResultFormatter;

import java.util.List;

public interface CommentOperationFormatter extends OperationResultFormatter<Comment> {
    String listInfo(List<Comment> comments);
}
