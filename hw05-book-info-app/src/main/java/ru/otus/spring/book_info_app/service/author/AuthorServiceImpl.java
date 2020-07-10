package ru.otus.spring.book_info_app.service.author;

import org.springframework.stereotype.Service;
import ru.otus.spring.book_info_app.dao.author.AuthorDao;
import ru.otus.spring.book_info_app.domain.Author;
import ru.otus.spring.book_info_app.infrastructure.AppLogger;
import ru.otus.spring.book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.book_info_app.service.result.Executed;
import ru.otus.spring.book_info_app.service.result.Failed;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final AppLogger logger = AppLoggerFactory.logger(AuthorServiceImpl.class);

    private final AuthorDao dao;

    public AuthorServiceImpl(AuthorDao dao) {
        this.dao = dao;
    }

    @Override
    public ServiceResult<Author> create(Author author) {
        try {
            return new Executed<>(dao.save(author));
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    public ServiceResult<Void> update(Author author) {
        try {
            dao.update(author);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }

    @Override
    public ServiceResult<Void> remove(long id) {
        try {
            dao.delete(id);

            return Executed.unit();
        } catch (Exception e) {
            logger.logException(e);

            return new Failed<>();
        }
    }
}
