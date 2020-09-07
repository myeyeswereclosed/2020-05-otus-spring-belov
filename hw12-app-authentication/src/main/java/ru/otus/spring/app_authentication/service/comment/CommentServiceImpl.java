package ru.otus.spring.app_authentication.service.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.app_authentication.domain.Comment;
import ru.otus.spring.app_authentication.infrastructure.AppLogger;
import ru.otus.spring.app_authentication.infrastructure.AppLoggerFactory;
import ru.otus.spring.app_authentication.repository.comment.CommentRepository;
import ru.otus.spring.app_authentication.service.result.Executed;
import ru.otus.spring.app_authentication.service.result.Failed;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final static AppLogger logger = AppLoggerFactory.logger(CommentServiceImpl.class);

    private final CommentRepository repository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.repository = commentRepository;
    }

    @Override
    @Transactional
    public ServiceResult<Comment> edit(Comment comment) {
        try {
            return
                repository
                    .update(comment)
                    .map(Executed::new)
                    .orElseGet(
                        () -> {
                            logger.warn("Comment with id = {} not found", comment.getId());

                            return Executed.empty();
                        }
                    );
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>(e.getMessage());
        }
    }

    @Override
    public ServiceResult<Comment> find(String id) {
        try {
            return
                repository
                    .findById(id)
                    .map(comment -> {
                        logger.info("Found comment {}", comment.toString());

                        return new Executed<>(comment);
                    })
                    .orElse(Executed.empty())
            ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<List<Comment>> findAll() {
        try {
            return new Executed<>(repository.findAll());
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<String> remove(String id) {
        try {
            return
                repository
                    .delete(id)
                    .map(Executed::new)
                    .orElseGet(
                        () -> {
                            logger.warn("Comment with id = {} not found", id);

                            return Executed.empty();
                        }
                    );
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
