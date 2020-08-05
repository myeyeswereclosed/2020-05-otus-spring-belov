package ru.otus.spring.web_ui_book_info_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.web_ui_book_info_app.controller.error_handler.ErrorHandler;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.dto.NotFoundEntity;
import ru.otus.spring.web_ui_book_info_app.service.book.BookService;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final static String HOME = "redirect:/";

    private final static String NOT_FOUND_TEMPLATE = "not_found";
    private final static String ERROR_TEMPLATE = "error";

    private final BookService service;
    private final ErrorHandler errorHandler;

    @GetMapping("/")
    public String list(Model model) {
        var result = service.getAll();

        return
            errorHandler
                .handle(result, ERROR_TEMPLATE)
                .orElse(
                    result
                        .value()
                        .map(
                            books -> {
                                model.addAttribute("books", books);

                                return "book/list";
                            }
                        )
                        .orElse("book/list")
                )
            ;
    }

    @GetMapping("/addBook")
    public String add(Model model) {
        model.addAttribute("book", new Book());

        return "book/add";
    }

    @PostMapping("/addBook")
    public String add(Book book, BindingResult bindingResult) {
        return
            errorHandler
                .handle(bindingResult, ERROR_TEMPLATE)
                .orElseGet(
                    () -> {
                        var serviceResult = service.addBook(book);

                        return
                            errorHandler
                                .handle(serviceResult, ERROR_TEMPLATE)
                                .orElse(
                                    serviceResult
                                        .value()
                                        .map(bookCreated -> HOME)
                                        .orElse(ERROR_TEMPLATE)
                                );
                    }
                );
    }

    @GetMapping("/editBook")
    public String edit(@RequestParam("id") String id, Model model) {
        model
            .addAttribute("book", new Book())
            .addAttribute("id", id);

        return "book/edit";
    }

    @PostMapping("/editBook/{id}")
    public String edit(Book book, BindingResult bindingResult, Model model) {
        return
            errorHandler
                .handle(bindingResult, ERROR_TEMPLATE)
                .orElseGet(
                    () -> {
                        var serviceResult = service.rename(book);

                        return
                            errorHandler
                                .handle(serviceResult, ERROR_TEMPLATE)
                                .orElse(
                                    serviceResult
                                        .value()
                                        .map(bookCreated -> HOME)
                                        .orElseGet(
                                            () -> {
                                                makeModel(model, book.getId());

                                                return NOT_FOUND_TEMPLATE;
                                            }
                                        )
                                    );
                    }
                );
    }

    @GetMapping("/removeBook")
    public String remove(@RequestParam("id") String id, Model model) {
        var serviceResult = service.remove(id);

        return
            errorHandler
                .handle(serviceResult, ERROR_TEMPLATE)
                .orElse(
                    serviceResult
                        .value()
                        .map(removed -> HOME)
                        .orElseGet(
                            () -> {
                                makeModel(model, id);

                                return NOT_FOUND_TEMPLATE;
                            }
                        )
                );
    }

    private void makeModel(Model model, String id) {
        model.addAttribute("entity", new NotFoundEntity("Book", id));
    }
}
