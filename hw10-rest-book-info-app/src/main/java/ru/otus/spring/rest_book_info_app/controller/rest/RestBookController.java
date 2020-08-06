package ru.otus.spring.rest_book_info_app.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.BookDto;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.service.book.BookService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@RestController
@RequiredArgsConstructor
public class RestBookController {
    private final BookService bookService;
    private final ResultHandler resultHandler;

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> all() {
        System.out.println("HERE TOO");

        var serviceResult = bookService.getAll();

        return
            resultHandler
                .handle(
                    serviceResult,
                    (books -> books.stream().map(BookDto::fromBook).collect(Collectors.toList())),
                    new ResponseEntity<>(emptyList(), HttpStatus.OK),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }
}
