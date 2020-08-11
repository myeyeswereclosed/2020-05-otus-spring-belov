package ru.otus.spring.rest_book_info_app.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.rest_book_info_app.controller.rest.dto.*;
import ru.otus.spring.rest_book_info_app.domain.*;
import ru.otus.spring.rest_book_info_app.service.book.info.add.AddBookInfoService;
import ru.otus.spring.rest_book_info_app.service.book.info.get.GetBookInfoService;
import ru.otus.spring.rest_book_info_app.service.result.Executed;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Rest-контроллер сводной информации по книгам должен ")
@SpringBootTest
@AutoConfigureMockMvc
public class RestBookInfoControllerTest {
    private static final String BOOK_ID_STUB = "JustAStubId";

    private static final Author AUTHOR = new Author("AuthorStubId", "Some", "Author");
    private static final Author NEW_AUTHOR = new Author("Another", "One");

    private static final Genre GENRE = new Genre("GenreStubId", "horror");
    private static final Genre NEW_GENRE = new Genre("drama");


    private static final Book BOOK =
        new Book(
            BOOK_ID_STUB,
            "Tri porosenka",
            new ArrayList<>(){{ add(AUTHOR); }},
            new ArrayList<>(){{ add(GENRE); }}
        );

    private static final Comment COMMENT = new Comment("Good book");

    private static final BookInfo BOOK_INFO = new BookInfo(BOOK, List.of(COMMENT));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GetBookInfoService getBookInfoService;

    @MockBean
    private AddBookInfoService addBookInfoService;

    @DisplayName("отдавать информацию о книге")
    @Test
    public void getInfo() throws Exception {
        given(getBookInfoService.get(BOOK.getId())).willReturn(new Executed<>(BOOK_INFO));

        mvc
            .perform(get("/book/" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .string(
                        equalTo(
                            new ObjectMapper().writeValueAsString(
                                new BookInfoDto(expectedBookDto(), List.of(COMMENT.getText()))
                            )
                        )
                    )
            )
        ;
    }

    @DisplayName("отдавать пустой результат при попытке запросить информацию по несохраненной книге")
    @Test
    public void getNonStored() throws Exception {
        given(getBookInfoService.get(BOOK.getId())).willReturn(Executed.empty());

        mvc
            .perform(get("/book/" + BOOK.getId()))
            .andExpect(status().is(404))
        ;
    }

    @DisplayName("добавлять автора сохраненной книги")
    @Test
    public void addBookAuthor() throws Exception {
        given(addBookInfoService.addBookAuthor(BOOK.getId(), NEW_AUTHOR))
            .willReturn(new Executed<>(BOOK.addAuthor(NEW_AUTHOR)));

        mvc
            .perform(
                post("/book/" + BOOK.getId() + "/author")
                    .param("firstName", NEW_AUTHOR.getFirstName())
                    .param("lastName", NEW_AUTHOR.getLastName())
            )
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .string(equalTo(new ObjectMapper().writeValueAsString(expectedBookDto())))
            )
        ;
    }

    private BookDto expectedBookDto() {
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

    @DisplayName("отдавать пустой результат при попытке добавить автора несохраненной книги")
    @Test
    public void addAuthorOfNonStoredBook() throws Exception {
        given(addBookInfoService.addBookAuthor(BOOK.getId(), NEW_AUTHOR))
            .willReturn(Executed.empty());

        mvc
            .perform(
                post("/book/" + BOOK.getId() + "/author")
                    .param("firstName", NEW_AUTHOR.getFirstName())
                    .param("lastName", NEW_AUTHOR.getLastName())
            )
            .andExpect(status().is(404))
        ;
    }

    @DisplayName("добавлять жанр сохраненной книги")
    @Test
    public void addBookGenre() throws Exception {
        given(addBookInfoService.addBookGenre(BOOK.getId(), NEW_GENRE))
            .willReturn(new Executed<>(BOOK.addGenre(NEW_GENRE)));

        mvc
            .perform(post("/book/" + BOOK.getId() + "/genre").param("name", NEW_GENRE.getName()))
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .string(equalTo(new ObjectMapper().writeValueAsString(expectedBookDto()))
            ))
        ;
    }

    @DisplayName("отдавать пустой результат при попытке добавить жанр несохраненной книги")
    @Test
    public void addGenreOfNonStoredBook() throws Exception {
        given(addBookInfoService.addBookGenre(BOOK.getId(), NEW_GENRE))
            .willReturn(Executed.empty());

        mvc
            .perform(post("/book/" + BOOK.getId() + "/genre").param("name", NEW_GENRE.getName()))
            .andExpect(status().is(404))
        ;
    }

    @DisplayName("добавлять комментарий к сохраненной книге")
    @Test
    public void addComment() throws Exception {
        given(addBookInfoService.addComment(BOOK.getId(), COMMENT))
            .willReturn(new Executed<>(new Comment(COMMENT.getText(), BOOK)));

        mvc
            .perform(post("/book/" + BOOK.getId() + "/comment").param("text", COMMENT.getText()))
            .andExpect(status().isOk())
            .andExpect(
                content()
                    .string(new ObjectMapper().writeValueAsString(new CommentDto(COMMENT.getText(), null)))
            )
        ;
    }

    @DisplayName("отдавать пустой результат при попытке добавить комментарий к несохраненной книге")
    @Test
    public void addCommentToNonStoredBook() throws Exception {
        given(addBookInfoService.addComment(BOOK.getId(), COMMENT))
            .willReturn(Executed.empty());

        mvc
            .perform(post("/book/" + BOOK.getId() + "/comment").param("text", COMMENT.getText()))
            .andExpect(status().is(404))
        ;
    }
}
