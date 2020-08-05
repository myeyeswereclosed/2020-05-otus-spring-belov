package ru.otus.spring.hw10_rest_book_info_app.controller.page;

import org.springframework.web.bind.annotation.GetMapping;

public class BookInfoPageController {
    @GetMapping("/")
    public String bookInfoList() {
        return "book_info/info_list";
    }
}
