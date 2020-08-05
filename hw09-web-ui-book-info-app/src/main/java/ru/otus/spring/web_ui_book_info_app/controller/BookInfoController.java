package ru.otus.spring.web_ui_book_info_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.dto.NotFoundEntity;
import ru.otus.spring.web_ui_book_info_app.service.book.info.add.AddBookInfoService;
import ru.otus.spring.web_ui_book_info_app.service.book.info.get.GetBookInfoService;
import ru.otus.spring.web_ui_book_info_app.service.result.ServiceResult;

@RequiredArgsConstructor
@Controller
public class BookInfoController {
    private final static String LIST_TEMPLATE = "book_info/info_list";
    private final static String BOOK_TEMPLATE = "book_info/info";

    private final static String NOT_FOUND_TEMPLATE = "not_found";
    private final static String ERROR_TEMPLATE = "error";

    private final static String HOME = "redirect:/";

    private final GetBookInfoService getInfoService;
    private final AddBookInfoService addInfoService;
    private final ErrorHandler errorHandler;

    @GetMapping("/")
    public String bookInfoList(Model model) {
        var result = getInfoService.getAll();

        return
            errorHandler
                .handle(result, ERROR_TEMPLATE)
                .orElse(
                    result
                        .value()
                        .map(bookInfoList -> {
                            model.addAttribute("bookInfoList", bookInfoList);

                            return LIST_TEMPLATE;
                        })
                        .orElse(LIST_TEMPLATE)
                )
        ;
    }

    @GetMapping("/info")
    public String bookInfo(@RequestParam("id") String id, Model model) {
        var result = getInfoService.get(id);

        return
            errorHandler
                .handle(result, ERROR_TEMPLATE)
                .orElse(
                    result
                        .value()
                        .map(
                            bookInfo -> {
                                model.addAttribute("bookInfo", bookInfo);

                                return BOOK_TEMPLATE;
                            }
                        )
                        .orElseGet(
                            () -> {
                                model.addAttribute("entity", new NotFoundEntity("Book", id));

                                return NOT_FOUND_TEMPLATE;
                            }
                        )
                );
    }

    @GetMapping("/addBookAuthor")
    public String addBookAuthor(@RequestParam("bookId") String bookId, Model model) {
        model
            .addAttribute("author", new Author())
            .addAttribute("bookId", bookId);

        return "book_info/add_book_author";
    }

    @PostMapping("/addBookAuthor/{bookId}")
    public String addBookAuthor(
        @PathVariable("bookId") String bookId,
        Author author,
        BindingResult bindingResult,
        Model model
    ) {
        return bookInfoOperation(bindingResult, addInfoService.addBookAuthor(bookId, author), model, bookId, HOME);
    }

    @GetMapping("/addBookGenre")
    public String addBookGenre(@RequestParam("bookId") String bookId, Model model) {
        model
            .addAttribute("genre", new Genre())
            .addAttribute("bookId", bookId);

        return "book_info/add_book_genre";
    }

    @PostMapping("/addBookGenre/{bookId}")
    public String addBookGenre(
        @PathVariable("bookId") String bookId,
        Genre genre,
        BindingResult bindingResult,
        Model model
    ) {
        return bookInfoOperation(bindingResult, addInfoService.addBookGenre(bookId, genre), model, bookId, HOME);
    }

    @GetMapping("/addComment")
    public String addComment(@RequestParam("bookId") String bookId, Model model) {
        model
            .addAttribute("comment", new Comment())
            .addAttribute("bookId", bookId);

        return "book_info/add_comment";
    }

    @PostMapping("/addComment/{bookId}")
    public String addComment(
        @PathVariable("bookId") String bookId,
        Comment comment,
        BindingResult bindingResult,
        Model model
    ) {
        return
            bookInfoOperation(
                bindingResult,
                addInfoService.addComment(bookId, comment),
                model,
                bookId,
                "redirect:/info?id=" + bookId
            );
    }

    private<T> String bookInfoOperation(
        BindingResult bindingResult,
        ServiceResult<T> serviceResult,
        Model model,
        String id,
        String onSuccessRedirectTo
    ) {
        return
            errorHandler
                .handle(bindingResult, ERROR_TEMPLATE)
                .orElseGet(
                    () ->
                        errorHandler
                            .handle(serviceResult, ERROR_TEMPLATE)
                            .orElse(
                                serviceResult
                                    .value()
                                    .map(operationExecuted -> onSuccessRedirectTo)
                                    .orElseGet(
                                        () -> {
                                            makeModel(model, id);

                                            return NOT_FOUND_TEMPLATE;
                                        }
                                    )
                            )
                );
    }

    private void makeModel(Model model, String id) {
        model.addAttribute("entity", new NotFoundEntity("Book", id));
    }
}
