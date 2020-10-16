package ru.otus.spring.hw18.book_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw18.book_service.controller.mapper.DtoMapper;
import ru.otus.spring.hw18.book_service.controller.result_handler.ServiceResultHandler;
import ru.otus.spring.hw18.book_service.domain.*;
import ru.otus.spring.hw18.book_service.service.book.info.add.AddBookInfoService;
import ru.otus.spring.hw18.book_service.service.book.info.get.GetBookInfoService;
import ru.otus.spring.hw18.lib.*;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookInfoController {
    private final GetBookInfoService bookInfoService;
    private final AddBookInfoService addBookInfoService;
    private final ServiceResultHandler resultHandler;

    private final DtoMapper<Book, BookDto> bookMapper;
    private final DtoMapper<BookInfo, BookInfoDto> bookInfoMapper;

    private final DtoMapper<Author, AuthorDto> authorMapper;
    private final DtoMapper<Genre, GenreDto> genreMapper;

    @GetMapping("/book/{id}")
    public ResponseEntity<BookInfoDto> get(@PathVariable String id) {
        return
            resultHandler
                .handle(
                    bookInfoService.get(id),
                    bookInfoMapper::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }

    @PostMapping("/book/{bookId}/author")
    public ResponseEntity<BookDto> addBookAuthor(@PathVariable String bookId, @RequestBody AuthorDto author) {
        return
            resultHandler
                .handle(
                    addBookInfoService.addBookAuthor(bookId, authorMapper.toEntity(author)),
                    bookMapper::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }

    @PostMapping("/book/{bookId}/genre")
    public ResponseEntity<BookDto> addBookGenre(@PathVariable String bookId, @RequestBody GenreDto genre) {
        return
            resultHandler
                .handle(
                    addBookInfoService.addBookGenre(bookId, genreMapper.toEntity(genre)),
                    bookMapper::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }

    @PostMapping("/book/{bookId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable String bookId, @RequestBody CommentDto dto) {
        return
            resultHandler
                .handle(
                    addBookInfoService.addComment(bookId, new Comment(dto.getText())),
                    Comment::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }
}
