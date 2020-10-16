package ru.otus.spring.hw18.comment_service.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw18.comment_service.domain.Comment;
import ru.otus.spring.hw18.comment_service.repository.CommentRepository;
import ru.otus.spring.hw18.lib.CommentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final CommentRepository repository;

    @GetMapping
    public List<Comment> byBookId(@RequestParam("bookId") String bookId) {
        logger.info("Incoming GET request, bookId = {}", bookId);

        return repository.findAllByBookId(bookId);
    }

    @PostMapping
    public CommentDto add(@RequestBody CommentDto comment) {
        logger.info("Incoming POST request, comment = {}", comment);

        return repository.save(Comment.fromDto(comment)).toDto();
    }
}
