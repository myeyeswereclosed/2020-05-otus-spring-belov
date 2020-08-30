package ru.otus.spring.reactive_book_info_app.controller.rest.handler.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.controller.rest.dto.BookDto;
import ru.otus.spring.reactive_book_info_app.controller.rest.handler.ErrorHandler;
import ru.otus.spring.reactive_book_info_app.controller.rest.mapper.DtoMapper;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.infrastructure.AppLogger;
import ru.otus.spring.reactive_book_info_app.infrastructure.AppLoggerFactory;
import ru.otus.spring.reactive_book_info_app.repository.book.BookRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.CommentRepository;
import ru.otus.spring.reactive_book_info_app.repository.comment.UpdateCommentConfig;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Service
@RequiredArgsConstructor
public class BookHandlerImpl implements BookHandler {
    private static final AppLogger logger = AppLoggerFactory.logger(BookHandlerImpl.class);

    private final ErrorHandler errorHandler;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final DtoMapper<Book, BookDto> mapper;

    @Override
    public Mono<ServerResponse> addBook(Book book) {
        return
            bookRepository
                .save(book)
                .then(ok().build())
                .onErrorResume(e -> errorHandler.serverError(logger, e))
            ;
    }

    @Override
    public Mono<ServerResponse> rename(Book book) {
        return
            bookRepository
                .update(book)
                .flatMap(
                    updated ->
                        commentRepository
                            .update(UpdateCommentConfig.renameBook(book))
                            .then(ok().build())
                            .onErrorResume(e -> errorHandler.serverError(logger, e))
                )
                .switchIfEmpty(errorHandler.notFound(logger, book.getId(), "rename()"))
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }

    @Override
    public Mono<ServerResponse> remove(String id) {
        return
            bookRepository
                .delete(id)
                .flatMap(
                    removed ->
                        commentRepository
                            .deleteAllByBook_Id(id)
                            .then(ok().build())
                            .onErrorResume(e -> errorHandler.serverError(logger, e))
                )
                .switchIfEmpty(errorHandler.notFound(logger, id, "remove()"))
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }

    @Override
    public Mono<ServerResponse> getAll() {
        return
            bookRepository
                .findAll()
                .map(mapper::toDto)
                .collectList()
                .flatMap(books -> ok().contentType(APPLICATION_JSON).body(fromValue(books)))
                .onErrorResume(e -> errorHandler.serverError(logger, e))
        ;
    }
}
