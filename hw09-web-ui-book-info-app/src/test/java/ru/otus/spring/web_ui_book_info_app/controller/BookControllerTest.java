package ru.otus.spring.web_ui_book_info_app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.dto.BookInfo;
import ru.otus.spring.web_ui_book_info_app.service.book.BookService;
import ru.otus.spring.web_ui_book_info_app.service.result.Executed;
import ru.otus.spring.web_ui_book_info_app.service.result.Failed;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер работы с книгой должен ")
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    private static final Book BOOK = new Book("Tri porosenka");
    private static final String BOOK_ID_STUB = "JustAStubId";

    private static final String NEW_TITLE = "Tri kotenka";

    private static final Author AUTHOR = new Author("Some", "Author");
    private static final Genre GENRE = new Genre("horror");

    private static final Book NEW_BOOK = new Book(BOOK_ID_STUB, NEW_TITLE, List.of(AUTHOR), List.of(GENRE));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @DisplayName("отдавать страницу с информацией о книгах")
    @Test
    public void infoList() throws Exception {
        given(bookService.getAll()).willReturn(new Executed<>(List.of(BOOK, NEW_BOOK)));

        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(BOOK.getTitle())))
            .andExpect(content().string(containsString(AUTHOR.getFirstName())))
            .andExpect(content().string(containsString(AUTHOR.getLastName())))
            .andExpect(content().string(containsString(GENRE.getName())))
            .andExpect(content().string(containsString(NEW_TITLE)))
        ;
    }

    @DisplayName("добавлять книгу ")
    @Test
    public void addBook() throws Exception {
        given(bookService.addBook(BOOK)).willReturn(new Executed<>(BOOK));

        mvc
            .perform(
                post("/addBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", BOOK.getTitle())
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(header().string("Location", "/"))
        ;
    }

    @DisplayName("редактировать сохраненную книгу")
    @Test
    public void editBook() throws Exception {
        var bookEdited = new Book(BOOK.getId(), NEW_TITLE);

        given(bookService.rename(bookEdited)).willReturn(new Executed<>(bookEdited));

        mvc
            .perform(
                post("/editBook/" + BOOK.getId())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("id", BOOK.getId())
                    .param("title", NEW_TITLE)
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(header().string("Location", "/"))
        ;
    }

    @DisplayName("удалять сохраненную книгу")
    @Test
    public void removeBook() throws Exception {
        given(bookService.remove(BOOK_ID_STUB)).willReturn(new Executed<>(BOOK_ID_STUB));

        mvc
            .perform(get("/removeBook?id=" + BOOK_ID_STUB))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(header().string("Location", "/"))
        ;
    }

    @DisplayName("отдавать страницу с сообщением, что книга не найдена, при попытке обновить несохраненную книгу")
    @Test
    public void editNonStoredBook() throws Exception {
        var bookEdited = new Book(BOOK.getId(), NEW_TITLE);

        given(bookService.rename(bookEdited)).willReturn(Executed.empty());

        mvc
            .perform(
                post("/editBook/" + BOOK.getId())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("id", BOOK.getId())
                    .param("title", NEW_TITLE)
            )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Book with id = " + BOOK.getId() + " not found")))
        ;
    }

    @DisplayName("отдавать страницу с сообщением, что книга не найдена, при попытке удалить несохраненную книгу")
    @Test
    public void removeNonStoredBook() throws Exception {
        given(bookService.remove(BOOK_ID_STUB)).willReturn(Executed.empty());

        mvc
            .perform(get("/removeBook?id=" + BOOK_ID_STUB))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Book with id = " + BOOK_ID_STUB + " not found")))
        ;
    }

    @DisplayName("отдавать страницу ошибки, если она произошла на уровне сервиса ")
    @Test
    public void serviceError() throws Exception {
        var action = get("/");

        given(bookService.getAll()).willReturn(new Failed<>());

        mvc
            .perform(action)
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Some error occurred. Please, try once more")));

    }
}
