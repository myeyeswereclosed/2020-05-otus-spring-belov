package ru.otus.spring.spring_data_jpa_book_info_app.service.comment;

import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Comment> edit(Comment comment);

    ServiceResult<Comment> find(long id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<Long> remove(long id);
}
