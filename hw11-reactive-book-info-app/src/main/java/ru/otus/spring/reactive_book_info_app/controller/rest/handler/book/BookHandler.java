package ru.otus.spring.reactive_book_info_app.controller.rest.handler.book;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Book;

public interface BookHandler {
    Mono<ServerResponse> addBook(Book book);

    Mono<ServerResponse> rename(Book book);

    Mono<ServerResponse> remove(String id);

    Mono<ServerResponse> getAll();
}
