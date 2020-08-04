package ru.otus.spring.web_ui_book_info_app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.web_ui_book_info_app.domain.Author;
import ru.otus.spring.web_ui_book_info_app.domain.Book;
import ru.otus.spring.web_ui_book_info_app.domain.Comment;
import ru.otus.spring.web_ui_book_info_app.domain.Genre;
import ru.otus.spring.web_ui_book_info_app.dto.BookInfo;
import ru.otus.spring.web_ui_book_info_app.service.book.info.add.AddBookInfoService;
import ru.otus.spring.web_ui_book_info_app.service.book.info.get.GetBookInfoService;
import ru.otus.spring.web_ui_book_info_app.service.result.Executed;
import ru.otus.spring.web_ui_book_info_app.service.result.Failed;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@DisplayName("Контроллер сводной информации о книгах должен ")
@SpringBootTest
@AutoConfigureMockMvc
public class BookInfoControllerTest {
    private static final String BOOK_ID_STUB = "just_a_stub_id";

    private static final Author AUTHOR = new Author("Some", "Author");
    private static final Genre GENRE = new Genre("horror");
    private static final Comment FIRST_COMMENT = new Comment("Good book");
    private static final Comment SECOND_COMMENT = new Comment("Super book");

    private static List<Comment> COMMENTS = List.of(FIRST_COMMENT, SECOND_COMMENT);

    private static final String TITLE = "Tri porosenka";
    private static final String ANOTHER_TITLE = "Tri kotenka";

    private static final Book BOOK = new Book(TITLE).addAuthor(AUTHOR).addGenre(GENRE);

    private static final BookInfo BOOK_INFO = new BookInfo(BOOK, COMMENTS);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GetBookInfoService getBookInfoService;

    @MockBean
    private AddBookInfoService addBookInfoService;

    @DisplayName("отдавать страницу с информацией о книге, если она сохранена")
    @Test
    public void info() throws Exception {
        given(getBookInfoService.get(BOOK_ID_STUB)).willReturn(new Executed<>(BOOK_INFO));

        bookExpected(mvc.perform(get("/info?id=" + BOOK_ID_STUB)));
    }

    private ResultActions bookExpected(ResultActions requestPerformed) throws Exception {
        return
            requestPerformed
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TITLE)))
                .andExpect(content().string(containsString(AUTHOR.getFirstName())))
                .andExpect(content().string(containsString(AUTHOR.getLastName())))
                .andExpect(content().string(containsString(GENRE.getName())))
                .andExpect(content().string(containsString(FIRST_COMMENT.getText())))
                .andExpect(content().string(containsString(SECOND_COMMENT.getText())))
        ;
    }

    @DisplayName("отдавать страницу с информацией, что книга не найдена ")
    @Test
    public void bookNotFound() throws Exception {
        given(getBookInfoService.get(BOOK_ID_STUB)).willReturn(Executed.empty());

        mvc
            .perform(get("/info?id=" + BOOK_ID_STUB))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Book with id = " + BOOK_ID_STUB + " not found")))
        ;
    }

    @DisplayName("отдавать страницу ошибки, если она произошла на уровне сервиса ")
    @Test
    public void serviceError() {
        var actions = List.of(get("/info?id=" + BOOK_ID_STUB), get("/"));

        given(getBookInfoService.get(BOOK_ID_STUB)).willReturn(new Failed<>());
        given(getBookInfoService.getAll()).willReturn(new Failed<>());

        actions.forEach(
            action -> {
                try {
                    mvc
                        .perform(action)
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("Some error occurred. Please, try once more")));
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            }
        );
    }

    @DisplayName("отдавать страницу с информацией о книгах")
    @Test
    public void infoList() throws Exception {
        given(getBookInfoService.getAll())
            .willReturn(
                new Executed<>(
                    List.of(
                        BOOK_INFO,
                        new BookInfo(
                            new Book(ANOTHER_TITLE),
                            Collections.emptyList()
                        )
                    )
                )
            );

        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(TITLE)))
            .andExpect(content().string(containsString(AUTHOR.getFirstName())))
            .andExpect(content().string(containsString(AUTHOR.getLastName())))
            .andExpect(content().string(containsString(GENRE.getName())))
            .andExpect(content().string(containsString(ANOTHER_TITLE)))
        ;
    }
}
