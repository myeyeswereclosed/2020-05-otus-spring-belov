package ru.otus.spring.hw18.book_info.controller.page;

import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.hw18.lib.BookDto;

@Controller
@RequiredArgsConstructor
public class BookController {
    private static final String SERVICE_NAME = "book-service";

    private final EurekaClient bookServiceClient;

    @GetMapping("/")
    public String books(Model model) {
        model.addAttribute("uri", requestUrl());

        return "book/list";
    }

    @GetMapping("/addBook")
    public String add(Model model) {
        model
            .addAttribute("book", new BookDto())
            .addAttribute("uri", String.format("%sbook", requestUrl()))
        ;

        return "book/add";
    }

    @GetMapping("/editBook")
    public String edit(@RequestParam("id") String id, @RequestParam("title") String title, Model model) {
        model
            .addAttribute("book", new BookDto())
            .addAttribute("id", id)
            .addAttribute("title", title)
            .addAttribute("uri", String.format("%sbook/%s", requestUrl(), id))
        ;

        return "book/edit";
    }

    private String requestUrl() {
        return bookServiceClient.getNextServerFromEureka(SERVICE_NAME, false).getHomePageUrl();
    }
}
