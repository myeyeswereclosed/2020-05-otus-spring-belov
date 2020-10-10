package ru.otus.spring.actuator.service.comment;

import ru.otus.spring.actuator.domain.Comment;
import ru.otus.spring.actuator.service.result.ServiceResult;

import java.util.List;

public interface CommentService {
    ServiceResult<Comment> edit(Comment comment);

    ServiceResult<Comment> find(String id);

    ServiceResult<List<Comment>> findAll();

    ServiceResult<String> remove(String id);
}
