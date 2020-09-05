package ru.otus.spring.reactive_book_info_app.controller.rest.handler.book_info;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Comment;
import ru.otus.spring.reactive_book_info_app.domain.Genre;

public interface BookInfoHandler {
    Mono<ServerResponse> getInfo(String bookId);

    Mono<ServerResponse> addAuthor(String bookId, Author author);

    Mono<ServerResponse> addGenre(String bookId, Genre genre);

    Mono<ServerResponse> addComment(String bookId, Comment comment);
}
