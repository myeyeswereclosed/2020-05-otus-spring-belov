package ru.otus.spring.hw18.book_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw18.book_service.controller.mapper.DtoMapper;
import ru.otus.spring.hw18.book_service.controller.result_handler.ServiceResultHandler;
import ru.otus.spring.hw18.book_service.domain.Book;
import ru.otus.spring.hw18.book_service.infrastructure.AppLogger;
import ru.otus.spring.hw18.book_service.infrastructure.AppLoggerFactory;
import ru.otus.spring.hw18.book_service.service.book.BookService;
import ru.otus.spring.hw18.lib.BookDto;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {
    private static final AppLogger logger = AppLoggerFactory.logger(BookController.class);

    private final BookService bookService;
    private final ServiceResultHandler resultHandler;
    private final DtoMapper<Book, BookDto> mapper;

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> all() {
        logger.info("Incoming {} all books request", "GET");

        return
            resultHandler
                .handle(
                    bookService.getAll(),
                    (books -> books.stream().map(mapper::toDto).collect(toList())),
                    new ResponseEntity<>(emptyList(), HttpStatus.OK),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }

    @PostMapping("/book")
    public ResponseEntity<BookDto> add(@RequestBody BookDto book) {
        logRequest(HttpMethod.POST, book);

        return
            resultHandler
                .handle(
                    bookService.addBook(mapper.toEntity(book)),
                    mapper::toDto,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> remove(@PathVariable String id) {
        logRequest(HttpMethod.DELETE, id);

        return
            resultHandler
                .handle(
                    bookService.remove(id),
                    removed -> removed,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> edit(@PathVariable String id, @RequestBody BookDto book) {
        logRequest(HttpMethod.PUT, book);

        book.setId(id);

        return
            resultHandler
                .handle(
                    bookService.rename(mapper.toEntity(book)),
                    mapper::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    private<T> void logRequest(HttpMethod method, T content) {
        logger.info("Incoming {} request, content: {}", method.name(), content.toString());
    }
}
