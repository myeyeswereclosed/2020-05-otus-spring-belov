package ru.otus.spring.actuator.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.actuator.controller.rest.dto.CommentDto;
import ru.otus.spring.actuator.domain.Author;
import ru.otus.spring.actuator.domain.Genre;

@Controller
public class BookInfoPageController {
    @GetMapping("/info")
    public String bookInfo(@RequestParam("id") String id, Model model) {
        model.addAttribute("id", id);

        return "book_info/info";
    }

    @GetMapping("/addBookAuthor")
    public String addBookAuthor(@RequestParam("bookId") String bookId, Model model) {
        model
            .addAttribute("author", new Author())
            .addAttribute("bookId", bookId);

        return "book_info/add_book_author";
    }

    @GetMapping("/addBookGenre")
    public String addBookGenre(@RequestParam("bookId") String bookId, Model model) {
        model
            .addAttribute("genre", new Genre())
            .addAttribute("bookId", bookId);

        return "book_info/add_book_genre";
    }

    @GetMapping("/addComment")
    public String addComment(@RequestParam("bookId") String bookId, Model model) {
        model
            .addAttribute("comment", new CommentDto())
            .addAttribute("bookId", bookId);

        return "book_info/add_comment";
    }
}
