package ru.otus.spring.rest_book_info_app.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookInfoPageController {
    @GetMapping("/")
    public String books() {
        System.out.println("I AM HERE");
        return "book/list1";
    }
}
