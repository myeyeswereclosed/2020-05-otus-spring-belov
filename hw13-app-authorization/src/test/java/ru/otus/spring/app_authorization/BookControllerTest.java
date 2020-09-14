package ru.otus.spring.app_authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import ru.otus.spring.app_authorization.controller.auth_handler.AuthHandler;
import ru.otus.spring.app_authorization.controller.auth_handler.AuthHandlerImpl;
import ru.otus.spring.app_authorization.controller.book.BookController;
import ru.otus.spring.app_authorization.controller.error_handler.ErrorHandler;
import ru.otus.spring.app_authorization.domain.Book;
import ru.otus.spring.app_authorization.security.user.AppPrincipal;
import ru.otus.spring.app_authorization.service.book.BookService;
import ru.otus.spring.app_authorization.service.result.Executed;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookController.class)
public class BookControllerTest {
    private final static String EDIT = "Edit";
    private final static String REMOVE = "Remove";
    private final static String ADD_BOOK = "Add book";
    private final static String LOGIN = "Login";
    private final static String REGISTER = "Register";
    private final static String LOGOUT = "Logout";
    private final static String DETAILS = "Details";
    private final static Book BOOK = new Book("StubId", "StubTitle");
    private final static AppPrincipal APP_PRINCIPAL =
        new AppPrincipal("Manager", "SomePassword", Collections.emptyList(), "Super", "Manager");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController controller;

    @MockBean
    private BookService bookService;

    @MockBean
    private ErrorHandler errorHandler;

    @MockBean
    private AuthHandlerImpl authHandler;

    @MockBean
    private UserDetailsService service;

    @DisplayName("позволять просматривать все книги неаутентифицированным пользователям без прав работы с книгами")
    @Test
    public void homePageNonAuthenticated() throws Exception {
        when(bookService.getAll()).thenReturn(new Executed<>(Collections.singletonList(BOOK)));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", "anonymousUser")
                        .addAttribute("isAuthenticated", false)
            );

        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForNonAuthenticatedUser(result.getResponse().getContentAsString()))
        ;
    }

    @DisplayName("позволять просматривать и производить действия над книгами менеджерам приложения")
    @Test
    public void managerHomePage() throws Exception {
        when(bookService.getAll()).thenReturn(new Executed<>(Collections.singletonList(BOOK)));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", APP_PRINCIPAL)
                        .addAttribute("isAuthenticated", true)
                        .addAttribute("canManage", true)
            );

        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForAuthenticatedUser(result.getResponse().getContentAsString()))
        ;
    }

    private void assertContentForNonAuthenticatedUser(String responseContent) {
        assertBookInfo(responseContent);

        assertThat(responseContent).contains(LOGIN);
        assertThat(responseContent).contains(REGISTER);
        assertThat(responseContent.contains(EDIT)).isFalse();
        assertThat(responseContent.contains(REMOVE)).isFalse();
        assertThat(responseContent.contains(ADD_BOOK)).isFalse();
    }

    private void assertContentForAuthenticatedUser(String responseContent) {
        assertBookInfo(responseContent);

        assertThat(responseContent).contains(APP_PRINCIPAL.fullName());
        assertThat(responseContent).contains(EDIT);
        assertThat(responseContent).contains(REMOVE);
        assertThat(responseContent).contains(ADD_BOOK);
        assertThat(responseContent).contains(LOGOUT);
    }

    private void assertBookInfo(String responseContent) {
        assertThat(responseContent).contains(BOOK.getId());
        assertThat(responseContent).contains(BOOK.getTitle());
        assertThat(responseContent).contains(DETAILS);
    }

    @DisplayName("редиректить на страницу аутентификации (логина)")
    @Test
    public void redirectToLoginPage() {
        var paths = Arrays.asList("/addBook", "/editBook", "/addBookAuthor", "/addBookGenre", "/removeBook");

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

    @DisplayName("редиректить на страницу аутентификации (логина)")
    @WithMockUser(
        username = "admin",
        authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void accessDenied() {
        var paths = Arrays.asList("/addBook", "/editBook", "/addBookAuthor", "/addBookGenre");

        paths.forEach(
            path -> {
                try {
                    mockMvc
                        .perform(get(path))
                        .andExpect(status().is4xxClientError())
                        .andExpect(
                            result ->
                                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value())
                        )
                    ;
                } catch (Exception e) {
                    fail();
                }
            }
        );
    }
}
