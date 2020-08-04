package ru.otus.spring.web_ui_book_info_app.service.comment;

import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Comment> edit(Comment comment);

    ServiceResult<Comment> find(String id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<String> remove(String id);
}
