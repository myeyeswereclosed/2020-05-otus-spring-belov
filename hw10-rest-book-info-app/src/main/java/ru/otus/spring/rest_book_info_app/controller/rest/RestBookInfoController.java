package ru.otus.spring.rest_book_info_app.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.*;
import ru.otus.spring.rest_book_info_app.controller.rest.mapper.DtoMapper;
import ru.otus.spring.rest_book_info_app.controller.rest.result_handler.ServiceResultHandler;
import ru.otus.spring.rest_book_info_app.domain.*;
import ru.otus.spring.rest_book_info_app.service.book.info.add.AddBookInfoService;
import ru.otus.spring.rest_book_info_app.service.book.info.get.GetBookInfoService;

@Controller
@RequiredArgsConstructor
public class RestBookInfoController {
    private final GetBookInfoService bookInfoService;
    private final AddBookInfoService addBookInfoService;
    private final ServiceResultHandler resultHandler;

    private final DtoMapper<Book, BookDto> bookMapper;
    private final DtoMapper<BookInfo, BookInfoDto> bookInfoMapper;

    private final DtoMapper<Author, AuthorDto> authorMapper;
    private final DtoMapper<Genre, GenreDto> genreMapper;
    private final DtoMapper<Comment, CommentDto> commentMapper;

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
    public ResponseEntity<BookDto> addBookAuthor(@PathVariable String bookId, AuthorDto author) {
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
    public ResponseEntity<BookDto> addBookGenre(@PathVariable String bookId, GenreDto genre) {
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
    public ResponseEntity<CommentDto> addComment(@PathVariable String bookId, CommentDto comment) {
        return
            resultHandler
                .handle(
                    addBookInfoService.addComment(bookId, commentMapper.toEntity(comment)),
                    commentMapper::toDto,
                    new ResponseEntity<>(HttpStatus.NOT_FOUND),
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
                )
            ;
    }
}
