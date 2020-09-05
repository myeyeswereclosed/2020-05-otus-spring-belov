package ru.otus.spring.reactive_book_info_app.repository.book;


import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.domain.Book;

public interface BookRepositoryCustom {
    Mono<Book> update(Book book);

    Mono<String> delete(String id);
}
