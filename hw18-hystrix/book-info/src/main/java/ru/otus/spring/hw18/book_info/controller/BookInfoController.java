package ru.otus.spring.hw18.book_info.controller;

import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.hw18.lib.AuthorDto;
import ru.otus.spring.hw18.lib.CommentDto;
import ru.otus.spring.hw18.lib.GenreDto;

@Controller
@RequiredArgsConstructor
public class BookInfoController {
    private static final String BOOK_SERVICE = "book-service";

    private final EurekaClient client;

    @GetMapping("/info")
    public String bookInfo(@RequestParam("id") String id, Model model) {
        model
            .addAttribute("id", id)
            .addAttribute("uri", String.format("%sbook/%s", requestUrl(), id));

        return "book_info/info";
    }

    @GetMapping("/addBookAuthor")
    public String addBookAuthor(@RequestParam("bookId") String bookId, Model model) {
        fillModel(model, bookId, "author", new AuthorDto(), "author");

        return "book_info/add_book_author";
    }

    @GetMapping("/addBookGenre")
    public String addBookGenre(@RequestParam("bookId") String bookId, Model model) {
        fillModel(model, bookId, "genre", new GenreDto(), "genre");

        return "book_info/add_book_genre";
    }

    @GetMapping("/addComment")
    public String addComment(@RequestParam("bookId") String bookId, Model model) {
        fillModel(model, bookId, "comment", new CommentDto(), "comment");

        return "book_info/add_comment";
    }

    private void fillModel(Model model, String bookId, String name, Object dto, String uriPath) {
        model
            .addAttribute(name, dto)
            .addAttribute("bookId", bookId)
            .addAttribute("uri", String.format("%sbook/%s/%s", requestUrl(), bookId, uriPath))
        ;
    }

    private String requestUrl() {
        return client.getNextServerFromEureka(BOOK_SERVICE, false).getHomePageUrl();
    }
}
