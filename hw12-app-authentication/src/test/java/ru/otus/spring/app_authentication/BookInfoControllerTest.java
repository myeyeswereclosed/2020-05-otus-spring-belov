package ru.otus.spring.app_authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import ru.otus.spring.app_authentication.controller.BookController;
import ru.otus.spring.app_authentication.controller.BookInfoController;
import ru.otus.spring.app_authentication.controller.auth_handler.AuthHandler;
import ru.otus.spring.app_authentication.controller.error_handler.ErrorHandler;
import ru.otus.spring.app_authentication.domain.Book;
import ru.otus.spring.app_authentication.domain.Comment;
import ru.otus.spring.app_authentication.dto.BookInfo;
import ru.otus.spring.app_authentication.security.user.AppPrincipal;
import ru.otus.spring.app_authentication.service.book.BookService;
import ru.otus.spring.app_authentication.service.book.info.add.AddBookInfoService;
import ru.otus.spring.app_authentication.service.book.info.get.GetBookInfoService;
import ru.otus.spring.app_authentication.service.result.Executed;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookInfoController.class)
public class BookInfoControllerTest {
    private final static String EDIT = "Edit";
    private final static String REMOVE = "Remove";
    private final static String LOGIN = "Login";
    private final static String LOGOUT = "Logout";
    private final static Book BOOK = new Book("StubId", "StubTitle");
    private final static List<Comment> COMMENTS =
        List.of(new Comment("FirstId", "Good book!"), new Comment("SecondId", "Super book"));
    private final static AppPrincipal APP_PRINCIPAL =
        new AppPrincipal("Manager", "SomePassword", Collections.emptyList(), "Super", "Manager");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookInfoController controller;

    @MockBean
    private GetBookInfoService getInfoService;

    @MockBean
    private AddBookInfoService addInfoService;

    @MockBean
    private ErrorHandler errorHandler;

    @MockBean
    private AuthHandler authHandler;

    @MockBean
    private UserDetailsService service;

    @DisplayName("позволять просматривать информацию о книге любым пользователям")
    @Test
    public void bookPageNonAuthenticated() throws Exception {
        when(getInfoService.get(BOOK.getId())).thenReturn(new Executed<>(new BookInfo(BOOK, COMMENTS)));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", "anonymousUser")
                        .addAttribute("isAnonymous", true)
            );

        mockMvc
            .perform(get("/info?id=" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForNonAuthenticatedUser(result.getResponse().getContentAsString()))
        ;
    }

    @DisplayName("позволять просматривать все книги аутентифицированным пользователям")
    @Test
    public void bookPageAuthenticated() throws Exception {
        when(getInfoService.get(BOOK.getId())).thenReturn(new Executed<>(new BookInfo(BOOK, COMMENTS)));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", APP_PRINCIPAL)
                        .addAttribute("isAnonymous", false)
            );

        mockMvc
            .perform(get("/info?id=" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForAuthenticatedUser(result.getResponse().getContentAsString()))
        ;
    }

    @DisplayName("добавлять комментарий любым пользователям")
    @Test
    public void addCommentNonAuthenticated() throws Exception {
        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", "anonymousUser")
                        .addAttribute("isAnonymous", true)
            );

        mockMvc
            .perform(get("/addComment?bookId=" + BOOK.getId()))
            .andExpect(status().isOk())
        ;
    }

    @DisplayName("добавлять комментарий аутентифицированным пользователям")
    @Test
    public void addCommentAuthenticated() throws Exception {
        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", "anonymousUser")
                        .addAttribute("isAnonymous", true)
            );

        mockMvc
            .perform(get("/addComment?bookId=" + BOOK.getId()))
            .andExpect(status().isOk())
        ;
    }

    private void assertContentForNonAuthenticatedUser(String responseContent) {
        assertBookInfo(responseContent);

        assertThat(responseContent).contains(LOGIN);
        assertThat(responseContent.contains(EDIT)).isFalse();
        assertThat(responseContent.contains(REMOVE)).isFalse();
    }

    private void assertContentForAuthenticatedUser(String responseContent) {
        assertBookInfo(responseContent);

        assertThat(responseContent).contains(APP_PRINCIPAL.fullName());
        assertThat(responseContent).contains(EDIT);
        assertThat(responseContent).contains(REMOVE);
        assertThat(responseContent).contains(LOGOUT);
    }

    private void assertBookInfo(String responseContent) {
        assertThat(responseContent).contains(BOOK.getId());
        assertThat(responseContent).contains(BOOK.getTitle());

        COMMENTS.forEach(comment -> assertThat(responseContent).contains(comment.getText()));
    }

    @DisplayName("редиректить на страницу аутентификации (логина)")
    @Test
    public void redirectToLoginPage() {
        var paths = Arrays.asList("/addBookAuthor", "/addBookGenre");

        paths.forEach(
            path -> {
                try {
                    mockMvc
                        .perform(get(path))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl()).endsWith("/login"))
                    ;
                } catch (Exception e) {
                    fail();
                }
            }
        );
    }
}
