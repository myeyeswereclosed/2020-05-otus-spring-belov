package ru.otus.spring.hw18.book_service.service.comment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.hw18.lib.CommentDto;

import java.util.List;

@FeignClient(name = "comment-service")
public interface CommentServiceClient {
    @GetMapping
    List<CommentDto> allByBookId(@RequestParam("bookId") String bookId);

    @PostMapping
    CommentDto add(@RequestBody CommentDto comment);
}
