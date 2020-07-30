package ru.otus.spring.mongo_db_book_info_app.service.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.mongo_db_book_info_app.domain.Author;
import ru.otus.spring.mongo_db_book_info_app.domain.Book;
import ru.otus.spring.mongo_db_book_info_app.domain.Comment;
import ru.otus.spring.mongo_db_book_info_app.domain.Genre;
import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.mongo_db_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.mongo_db_book_info_app.repository.AuthorRepository;
import ru.otus.spring.mongo_db_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.mongo_db_book_info_app.repository.GenreRepository;
import ru.otus.spring.mongo_db_book_info_app.repository.book.BookRepository;
import ru.otus.spring.mongo_db_book_info_app.service.result.Executed;
import ru.otus.spring.mongo_db_book_info_app.service.result.Failed;
import ru.otus.spring.mongo_db_book_info_app.service.result.ServiceResult;


import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class BookInfoServiceImpl implements BookInfoService {
    private static final AppLogger logger = AppLoggerFactory.logger(BookInfoServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<BookInfo> get(String bookId) {
        try {
            return
                bookRepository
                    .findById(bookId)
                    .map(
                        book ->
                            new Executed<>(
                                new BookInfo(
                                    book,
                                    commentRepository.findAllByBook_Id(bookId)
                                )
                            )
                    )
                    .orElse(Executed.empty())
                ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<List<BookInfo>> getAll() {
        try {
            var comments = comments();

            return
                new Executed<>(
                    bookRepository
                        .findAll()
                        .stream()
                        .map(
                            book ->
                                new BookInfo(
                                    book,
                                    comments.getOrDefault(book.getId(), Collections.emptyList())
                                )
                        )
                        .collect(toList())
                );
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
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
                    .map(
                        authorFound ->
                            updateBookAuthor(book, authorFound, "Added existing author {} as author of '{}'")
                    )
                    .orElseGet(() -> addBookNewAuthor(book, author, "Added new author {} as author of '{}'"))
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

    private ServiceResult<Book> updateBookAuthor(Book book, Author author, String logMessage) {
        try {
            var updatedBook = bookRepository.save(book.addAuthor(author));

            logger.info(logMessage, author.fullName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> addBookNewAuthor(Book book, Author author, String logMessage) {
        try {
            var updatedBook =
                bookRepository.save(
                    book.addAuthor(authorRepository.save(author))
                );

            logger.info(logMessage, author.fullName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
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
                    .map(genreFound -> updateBookGenre(book, genreFound, "Added '{}' as genre of '{}'"))
                    .orElseGet(() -> addBookNewGenre(book, genre, "Added new genre '{}' as genre of '{}'"))
            ;
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    private ServiceResult<Book> updateBookGenre(Book book, Genre genre, String logMessage) {
        var updatedBook = bookRepository.save(book.addGenre(genre));

        logger.info(logMessage, genre.getName(), updatedBook.toString());

        return new Executed<>(updatedBook);
    }

    private ServiceResult<Book> addBookNewGenre(Book book, Genre genre, String logMessage) {
        try {
            var updatedBook =
                bookRepository.save(
                    book.addGenre(genreRepository.save(genre))
                );

            logger.info(logMessage, genre.getName(), updatedBook.toString());

            return new Executed<>(updatedBook);
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }

    @Override
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

    private Map<String, List<Comment>> comments() {
        return
            commentRepository
                .findAll()
                .parallelStream()
                .collect(
                    Collectors.groupingBy(
                        comment -> comment.getBook().getId(),
                        Collectors.toList()
                    )
                )
            ;
    }
}
