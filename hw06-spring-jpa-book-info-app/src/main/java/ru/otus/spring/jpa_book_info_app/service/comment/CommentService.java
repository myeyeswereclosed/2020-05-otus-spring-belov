package ru.otus.spring.jpa_book_info_app.service.comment;

import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Void> edit(Comment comment);

    ServiceResult<Comment> find(long id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<Void> remove(long id);
}
