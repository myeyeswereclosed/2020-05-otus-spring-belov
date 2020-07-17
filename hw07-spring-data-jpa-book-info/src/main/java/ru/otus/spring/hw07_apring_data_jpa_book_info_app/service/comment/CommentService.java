package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.comment;

import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Void> edit(Comment comment);

    ServiceResult<Comment> find(long id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<Void> remove(long id);
}
