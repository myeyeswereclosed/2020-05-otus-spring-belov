package ru.otus.spring.app_authorization.service.comment;

import ru.otus.spring.app_authorization.domain.Comment;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Comment> edit(Comment comment);

    ServiceResult<Comment> find(String id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<String> remove(String id);
}
