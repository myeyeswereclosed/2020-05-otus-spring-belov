package ru.otus.spring.actuator.service.book.info.add;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.domain.Book;
import ru.otus.spring.actuator.domain.Comment;
import ru.otus.spring.actuator.domain.Genre;
import ru.otus.spring.actuator.infrastructure.AppLogger;
import ru.otus.spring.actuator.infrastructure.AppLoggerFactory;
import ru.otus.spring.actuator.repository.author.AuthorRepository;
import ru.otus.spring.actuator.repository.book.BookRepository;
import ru.otus.spring.actuator.repository.comment.CommentRepository;
import ru.otus.spring.actuator.repository.comment.UpdateCommentConfig;
import ru.otus.spring.actuator.repository.genre.GenreRepository;
import ru.otus.spring.actuator.service.result.Executed;
import ru.otus.spring.actuator.service.result.Failed;
import ru.otus.spring.actuator.service.result.ServiceResult;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Service
public class AddBookInfoServiceImpl implements AddBookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(AddBookInfoServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ServiceResult<Book> addBookAuthor(String bookId, Author author) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(book -> addAuthor(book, author))
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addAuthor(@NotNull Book book, @NotNull Author author) {
        try {
            if (book.isWrittenBy(author)) {
                return addExistingInfo(book, "Book {} already has author {}", author.fullName());
            }

            return
                authorRepository
                    .findByFirstNameAndLastName(author.getFirstName(), author.getLastName())
                    .map(authorFound -> addStoredAuthor(book, authorFound))
                    .orElseGet(() -> addBookNewAuthor(book, author))
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private<T> ServiceResult<Book> addExistingInfo(Book book, String logMessage, String info) {
        logger.warn(logMessage, book, info);

        return new Executed<>(book);
    }

    private ServiceResult<Book> addStoredAuthor(Book book, Author author) {
        try {
            var updatedBook = bookRepository.save(book.addAuthor(author));
            commentRepository.updateWithConfig(UpdateCommentConfig.addAuthor(book.getId(), author));

            logger.info("Added existing author {} as author of '{}'", author.fullName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addBookNewAuthor(Book book, Author author) {
        try {
            var newAuthor = authorRepository.save(author);

            var updatedBook = bookRepository.save(book.addAuthor(newAuthor));
            commentRepository.updateWithConfig(UpdateCommentConfig.addAuthor(book.getId(), newAuthor));

            logger.info("Added new author {} as author of '{}'", author.fullName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<Book> addBookGenre(String bookId, Genre genre) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(book -> addGenre(book, genre))
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addGenre(@NotNull Book book, @NotNull Genre genre) {
        try {
            if (book.hasGenre(genre)) {
                return addExistingInfo(book, "Book {} already has genre {}", genre.getName());
            }

            return
                genreRepository
                    .findByName(genre.getName())
                    .map(genreFound -> addStoredGenre(book, genreFound))
                    .orElseGet(() -> addBookNewGenre(book, genre))
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addStoredGenre(Book book, Genre genre) {
        var updatedBook = bookRepository.save(book.addGenre(genre));
        commentRepository.updateWithConfig(UpdateCommentConfig.addGenre(book.getId(), genre));

        logger.info("Added '{}' as genre of '{}'", genre.getName(), updatedBook.toString());

        return new Executed<>(updatedBook);
    }

    private ServiceResult<Book> addBookNewGenre(Book book, Genre genre) {
        try {
            var newGenre = genreRepository.save(genre);

            var updatedBook = bookRepository.save(book.addGenre(newGenre));
            commentRepository.updateWithConfig(UpdateCommentConfig.addGenre(book.getId(), newGenre));

            logger.info("Added new genre '{}' as genre of '{}'", genre.getName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional
    public ServiceResult<Comment> addComment(String bookId, Comment comment) {
        try {
            return
                bookRepository.findById(bookId)
                    .<ServiceResult<Comment>>map(
                        book -> {
                            comment.setBook(book);

                            commentRepository.save(comment);

                            logger.info("Added '{}' as comment to '{}'", comment.getText(), book.getTitle());

                            return new Executed<>(comment);
                        }
                    )
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
