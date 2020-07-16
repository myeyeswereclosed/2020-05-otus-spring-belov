package ru.otus.spring.jpa_book_info_app.service.comment;

import org.springframework.stereotype.Service;
import ru.otus.spring.jpa_book_info_app.domain.Comment;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.jpa_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.jpa_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.jpa_book_info_app.service.result.Executed;
import ru.otus.spring.jpa_book_info_app.service.result.Failed;
import ru.otus.spring.jpa_book_info_app.service.result.ServiceResult;

import javax.transaction.Transactional;
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
    public ServiceResult<Void> edit(Comment comment) {
        try {
            repository.update(comment);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<Comment> find(long id) {
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
    @Transactional
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
    public ServiceResult<Void> remove(long id) {
        try {
            if (repository.delete(id)) {
                return Executed.unit();
            }

            logger.warn("Comment with id = {} not found", id);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
