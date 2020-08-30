package ru.otus.spring.reactive_book_info_app.controller.rest.handler.book_info;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.BookDto;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.BookInfoDto;
import ru.otus.spring.reactive_book_info_app.controller.rest.handler.ErrorHandler;
import ru.otus.spring.reactive_book_info_app.controller.rest.mapper.DtoMapper;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.domain.Comment;
import ru.otus.spring.reactive_book_info_app.domain.Genre;
import ru.otus.spring.reactive_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.reactive_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.reactive_book_info_app.repository.author.AuthorRepository;
import ru.otus.spring.reactive_book_info_app.repository.book.BookRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.UpdateCommentConfig;
import ru.otus.spring.reactive_book_info_app.repository.genre.GenreRepository;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@RequiredArgsConstructor
@Component
public class BookInfoHandlerImpl implements BookInfoHandler {
    private static final AppLogger logger = AppLoggerFactory.logger(BookInfoHandlerImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    private final DtoMapper<Book, BookDto> mapper;
    private final ErrorHandler errorHandler;

    @Override
    public Mono<ServerResponse> addAuthor(String bookId, Author author) {
        return
            bookRepository
                .findById(bookId)
                .flatMap(
                    book -> {
                        if (book.isWrittenBy(author)) {
                            return addExistingInfo(book, "Book {} already has author {}", author.fullName());
                        }

                        return
                            authorRepository
                                .findByFirstNameAndLastName(author.getFirstName(), author.getLastName())
                                .flatMap(authorFound -> addStoredAuthor(book, authorFound, "Added existing author {} as author of '{}'"))
                                .switchIfEmpty(Mono.defer(() -> addNewAuthor(book, author)))
                                .onErrorResume(e -> errorHandler.serverError(logger, e))
                            ;
                    }
                )
                .switchIfEmpty(errorHandler.notFound(logger, bookId, "addAuthor()"))
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }

    private Mono<ServerResponse> addStoredAuthor(Book book, Author author, String logMessage) {
        return
            bookRepository
                .save(book.addAuthor(author))
                .flatMap(
                    bookUpdated ->
                        commentRepository
                            .update(UpdateCommentConfig.addAuthor(book.getId(), author))
                            .flatMap(
                                updateResult -> {
                                    logger.info(logMessage, author.fullName(), bookUpdated.toString());

                                    return ok().build();
                                }
                            )
                            .onErrorResume(e -> errorHandler.serverError(logger, e))
                )
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }

    private Mono<ServerResponse> addNewAuthor(Book book, Author author) {
        return
            authorRepository
                .save(author)
                .flatMap(newAuthor -> addStoredAuthor(book, newAuthor, "Added new author {} as author of '{}'"))
                .switchIfEmpty(status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }

    private Mono<ServerResponse> addExistingInfo(Book book, String logMessage, String info) {
        logger.warn(logMessage, book, info);

        return ServerResponse.ok().build();
    }

    @Override
    public Mono<ServerResponse> addGenre(String bookId, Genre genre) {
        return
            bookRepository
                .findById(bookId)
                .flatMap(
                    book -> {
                        if (book.hasGenre(genre)) {
                            return addExistingInfo(book, "Book {} already has author {}", genre.getName());
                        }

                        return
                            genreRepository
                                .findByName(genre.getName())
                                .flatMap(genreFound -> addStoredGenre(book, genreFound, "Added existing genre {} as genre of '{}'"))
                                .switchIfEmpty(Mono.defer(() -> addNewGenre(book, genre)))
                                .onErrorResume(e -> errorHandler.serverError(logger, e))
                            ;
                    }
                )
                .switchIfEmpty(errorHandler.notFound(logger, bookId, "addGenre()"))
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }

    private Mono<ServerResponse> addStoredGenre(Book book, Genre genre, String logMessage) {
        return
            bookRepository
                .save(book.addGenre(genre))
                .flatMap(
                    bookUpdated ->
                        commentRepository
                            .update(UpdateCommentConfig.addGenre(book.getId(), genre))
                            .flatMap(
                                updateResult -> {
                                    logger.info(logMessage, genre.getName(), bookUpdated.toString());

                                    return ok().build();
                                }
                            )
                            .onErrorResume(e -> errorHandler.serverError(logger, e))
                )
                .onErrorResume(t -> status(HttpStatus.INTERNAL_SERVER_ERROR).build())
            ;
    }

    private Mono<ServerResponse> addNewGenre(Book book, Genre genre) {
        return
            genreRepository
                .save(genre)
                .flatMap(newGenre -> addStoredGenre(book, newGenre, "Added new genre {} as genre of '{}'"))
                .switchIfEmpty(status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .onErrorResume(t -> status(HttpStatus.INTERNAL_SERVER_ERROR).build())
            ;
    }

    @Override
    public Mono<ServerResponse> addComment(String bookId, Comment comment) {
        return
            bookRepository
                .findById(bookId)
                .flatMap(
                    book -> {
                        comment.setBook(book);

                        return
                            commentRepository
                                .save(comment)
                                .flatMap(commentStored -> ok().build())
                                .onErrorResume(e -> errorHandler.serverError(logger, e));
                    }
                )
                .switchIfEmpty(errorHandler.notFound(logger, bookId, "addComment()"))
        ;
    }

    @Override
    public Mono<ServerResponse> getInfo(String bookId) {
        return
            bookRepository
                .findById(bookId)
                .flatMap(
                    book -> commentRepository
                        .findAllByBook_Id(bookId)
                        .collectList()
                        .map(
                            comments ->
                                new BookInfoDto(
                                    mapper.toDto(book),
                                    comments.stream().map(Comment::getText).collect(toList())
                                )
                        )
                        .flatMap(bookInfo -> ok().contentType(APPLICATION_JSON).body(fromValue(bookInfo)))
                        .onErrorResume(e -> errorHandler.serverError(logger, e))
                )
                .switchIfEmpty(errorHandler.notFound(logger, bookId, "getInfo()"))
            ;
    }
}
