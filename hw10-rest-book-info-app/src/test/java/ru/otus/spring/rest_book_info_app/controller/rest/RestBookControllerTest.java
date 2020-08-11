package ru.otus.spring.rest_book_info_app.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.AuthorDto;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.BookDto;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.GenreDto;
import ru.otus.spring.rest_book_info_app.domain.Author;
import ru.otus.spring.rest_book_info_app.domain.Book;
import ru.otus.spring.rest_book_info_app.domain.Genre;
import ru.otus.spring.rest_book_info_app.service.book.BookService;
import ru.otus.spring.rest_book_info_app.service.result.Executed;
import ru.otus.spring.rest_book_info_app.service.result.Failed;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Rest-контроллер запросов по книгам должен ")
@SpringBootTest
@AutoConfigureMockMvc
public class RestBookControllerTest {
    private static final String BOOK_ID_STUB = "JustAStubId";
    private static final Book BOOK = new Book(BOOK_ID_STUB, "Tri porosenka");

    private static final String NEW_TITLE = "Tri kotenka";

    private static final Author AUTHOR = new Author("Some", "Author");
    private static final Genre GENRE = new Genre("horror");

    private static final Book NEW_BOOK = new Book(BOOK_ID_STUB, NEW_TITLE, List.of(AUTHOR), List.of(GENRE));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @DisplayName("отдавать успешный ответ со списком книг")
    @Test
    public void getAll() throws Exception {
        var books = List.of(BOOK, NEW_BOOK);

        given(bookService.getAll()).willReturn(new Executed<>(books));

        mvc
            .perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo(new ObjectMapper().writeValueAsString(books))))
        ;
    }

    @DisplayName("отдавать ошибку, если она произошла на уровне сервиса")
    @Test
    public void getAllWithError() throws Exception {
        given(bookService.getAll()).willReturn(new Failed<>());

        mvc
            .perform(get("/books"))
            .andExpect(status().is5xxServerError())
        ;
    }

    @DisplayName("успешно добавлять новую книгу")
    @Test
    public void addBook() throws Exception {
        given(bookService.addBook(new Book(BOOK.getTitle()))).willReturn(new Executed<>(BOOK));

        mvc
            .perform(post("/book").param("title", BOOK.getTitle()))
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .string(equalTo(new ObjectMapper().writeValueAsString(expectedDto())))
            )
        ;
    }

    private BookDto expectedDto() {
        return
            new BookDto(
                BOOK.getId(),
                BOOK.getTitle(),
                BOOK
                    .getAuthors()
                    .stream()
                    .map(author -> new AuthorDto(author.getId(), author.getFirstName(), author.getLastName()))
                    .collect(toList()),
                BOOK
                    .getGenres()
                    .stream()
                    .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                    .collect(toList())
            );
    }

    @DisplayName("успешно редактировать сохраненную книгу")
    @Test
    public void edit() throws Exception {
        given(bookService.rename(BOOK)).willReturn(new Executed<>(BOOK));

        mvc
            .perform(
                put("/book/" + BOOK.getId())
                    .param("id", BOOK.getId())
                    .param("title", BOOK.getTitle())
            )
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .string(equalTo(new ObjectMapper().writeValueAsString(expectedDto())))
            )
        ;
    }

    @DisplayName("отдавать пустой результат при попытке обновить несохраненную книгу")
    @Test
    public void editNonStored() throws Exception {
        given(bookService.rename(BOOK)).willReturn(Executed.empty());

        mvc
            .perform(
                put("/book/" + BOOK.getId())
                    .param("id", BOOK.getId())
                    .param("title", BOOK.getTitle())
            )
            .andExpect(status().is(404))
        ;
    }

    @DisplayName("успешно удалять сохраненную книгу")
    @Test
    public void remove() throws Exception {
        given(bookService.remove(BOOK.getId())).willReturn(new Executed<>(BOOK.getId()));

        mvc
            .perform(delete("/book/" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo(BOOK.getId())))
        ;
    }

    @DisplayName("отдавать пустой результат при попытке удалить несохраненную книгу")
    @Test
    public void removeNonStored() throws Exception {
        given(bookService.remove(BOOK.getId())).willReturn(Executed.empty());

        mvc
            .perform(delete("/book/" + BOOK.getId()))
            .andExpect(status().is(404))
        ;
    }
}
