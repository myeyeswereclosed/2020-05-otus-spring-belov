package ru.otus.spring.app_container.service.comment;

import ru.otus.spring.app_container.domain.Comment;
import ru.otus.spring.app_container.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Comment> edit(Comment comment);

    ServiceResult<Comment> find(String id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<String> remove(String id);
}
