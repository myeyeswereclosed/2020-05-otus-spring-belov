package ru.otus.spring.rest_book_info_app.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.BookDto;
import ru.otus.spring.rest_book_info_app.controller.rest.mapper.DtoMapper;
import ru.otus.spring.rest_book_info_app.controller.rest.result_handler.ServiceResultHandler;
import ru.otus.spring.rest_book_info_app.domain.Book;
import ru.otus.spring.rest_book_info_app.service.book.BookService;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class RestBookController {
    private final BookService bookService;
    private final ServiceResultHandler resultHandler;
    private final DtoMapper<Book, BookDto> mapper;

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> all() {
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
    public ResponseEntity<BookDto> add(BookDto book) {
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
    public ResponseEntity<BookDto> edit(BookDto book) {
        return
            resultHandler
                .handle(
                    bookService.rename(mapper.toEntity(book)),
                    mapper::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }
}
