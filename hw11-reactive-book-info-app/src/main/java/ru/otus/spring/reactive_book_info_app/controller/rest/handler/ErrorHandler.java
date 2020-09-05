package ru.otus.spring.reactive_book_info_app.controller.rest.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.reactive_book_info_app.infrastructure.AppLogger;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class ErrorHandler {
    public Mono<ServerResponse> notFound(AppLogger logger, String bookId, String method) {
        return Mono.defer(
            () -> {
                logger.warn("{} - {}: Book with id = {} not found", logger.getLogger().getName(), method, bookId);

                return status(NOT_FOUND).build();
            }
        );
    }

    public Mono<ServerResponse> serverError(AppLogger logger, Throwable e) {
        logger.logException(e);

        return status(INTERNAL_SERVER_ERROR).build();
    }
}
