package ru.otus.spring.reactive_book_info_app.controller.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.reactive_book_info_app.controller.rest.handler.book.BookHandler;
import ru.otus.spring.reactive_book_info_app.controller.rest.handler.book_info.BookInfoHandler;
import ru.otus.spring.reactive_book_info_app.domain.Author;
import ru.otus.spring.reactive_book_info_app.domain.Book;
import ru.otus.spring.reactive_book_info_app.domain.Comment;
import ru.otus.spring.reactive_book_info_app.domain.Genre;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class Router {
    @Bean
    public RouterFunction<ServerResponse> composedRoutes(BookInfoHandler bookInfoHandler, BookHandler bookHandler) {
        return
            route()
                .GET("/books", request -> bookHandler.getAll())
                .GET("/book/{id}", request -> bookInfoHandler.getInfo(request.pathVariable("id")))
                .POST(
                    "/book",
                    accept(APPLICATION_FORM_URLENCODED),
                    request ->
                        request
                            .formData()
                            .flatMap(valuesMap -> bookHandler.addBook(new Book(valuesMap.getFirst("title"))))
                )
                .PUT(
                    "/book/{id}",
                    accept(APPLICATION_FORM_URLENCODED),
                    request ->
                        request
                            .formData()
                            .flatMap(
                                valuesMap ->
                                    bookHandler.rename(
                                        new Book(request.pathVariable("id"), valuesMap.getFirst("title"))
                                    )
                            )
                )
                .DELETE("/book/{id}", request -> bookHandler.remove(request.pathVariable("id")))
                .POST(
                    "/book/{bookId}/author",
                    accept(APPLICATION_FORM_URLENCODED),
                    request ->
                        request
                            .formData()
                            .flatMap(
                                valuesMap ->
                                    bookInfoHandler.addAuthor(
                                        request.pathVariable("bookId"),
                                        new Author(valuesMap.getFirst("firstName"), valuesMap.getFirst("lastName"))
                                    )
                            )
                )
                .POST(
                    "/book/{bookId}/genre",
                    accept(APPLICATION_FORM_URLENCODED),
                    request ->
                        request
                            .formData()
                            .flatMap(
                                valuesMap ->
                                    bookInfoHandler.addGenre(
                                        request.pathVariable("bookId"),
                                        new Genre(valuesMap.getFirst("name"))
                                    )
                            )
                )
                .POST(
                    "/book/{bookId}/comment",
                    accept(APPLICATION_FORM_URLENCODED),
                    request ->
                        request
                            .formData()
                            .flatMap(
                                valuesMap ->
                                    bookInfoHandler.addComment(
                                        request.pathVariable("bookId"),
                                        new Comment(valuesMap.getFirst("text"))
                                    )
                            )
                )
                .build()
        ;
    }
}
