package ru.otus.spring.app_authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import ru.otus.spring.app_authorization.controller.auth_handler.AuthHandler;
import ru.otus.spring.app_authorization.controller.book.BookInfoController;
import ru.otus.spring.app_authorization.controller.error_handler.ErrorHandler;
import ru.otus.spring.app_authorization.domain.Book;
import ru.otus.spring.app_authorization.domain.Comment;
import ru.otus.spring.app_authorization.dto.BookInfo;
import ru.otus.spring.app_authorization.security.user.AppPrincipal;
import ru.otus.spring.app_authorization.security.user.Role;
import ru.otus.spring.app_authorization.service.book.info.add.AddBookInfoService;
import ru.otus.spring.app_authorization.service.book.info.get.GetBookInfoService;
import ru.otus.spring.app_authorization.service.result.Executed;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер информации о книге должен ")
@WebMvcTest(BookInfoController.class)
public class BookInfoControllerTest {
    private final static String EDIT = "Edit";
    private final static String REMOVE = "Remove";
    private final static String LOGIN = "Login";
    private final static String REGISTER = "Register";
    private final static String LOGOUT = "Logout";
    private final static Book BOOK = new Book("StubId", "StubTitle");
    private final static List<Comment> COMMENTS =
        List.of(new Comment("FirstId", "Good book!"), new Comment("SecondId", "Super book"));

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
                        .addAttribute("isAuthenticated", false)
            );

        mockMvc
            .perform(get("/info?id=" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForNonAuthenticatedUser(result.getResponse().getContentAsString()))
        ;
    }

    @DisplayName("позволять просматривать информацию о книге обычным авторизованным пользователям")
    @Test
    public void bookPageForCommonUser() throws Exception {
        when(getInfoService.get(BOOK.getId())).thenReturn(new Executed<>(new BookInfo(BOOK, COMMENTS)));

        var principal = createPrincipal(Role.USER);

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", principal)
                        .addAttribute("isAuthenticated", true)
                        .addAttribute("canComment", true)
            );

        mockMvc
            .perform(get("/info?id=" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForCommonUser(result.getResponse().getContentAsString(), principal))
        ;
    }

    private AppPrincipal createPrincipal(Role role) {
        return
            new AppPrincipal(
                "StubPrincipal",
                "SomePassword",
                Collections.singletonList(new SimpleGrantedAuthority(role.toSecurityRole())),
                "StubName",
                "StubLastName"
            );
    }

    @DisplayName("позволять просматривать и редактировать книгу менеджеру")
    @Test
    public void bookPageForManager() throws Exception {
        var principal = createPrincipal(Role.MANAGER);

        when(getInfoService.get(BOOK.getId())).thenReturn(new Executed<>(new BookInfo(BOOK, COMMENTS)));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", principal)
                        .addAttribute("isAuthenticated", true)
                        .addAttribute("canManage", true)
            );

        mockMvc
            .perform(get("/info?id=" + BOOK.getId()))
            .andExpect(status().isOk())
            .andExpect(result -> assertContentForManager(result.getResponse().getContentAsString(), principal))
        ;
    }

    @DisplayName("позволять добавлять комментарий анонимным пользователям")
    @Test
    public void addCommentNonAuthenticated() throws Exception {
        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", "anonymousUser")
                        .addAttribute("isAuthenticated", false)
            );

        mockMvc
            .perform(get("/addComment?bookId=" + BOOK.getId()))
            .andExpect(status().isOk())
        ;
    }

    @DisplayName("позволять добавлять комментарий авторизованным пользователям")
    @Test
    public void addCommentAuthenticated() throws Exception {
        var principal = createPrincipal(Role.USER);

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", principal)
                        .addAttribute("isAuthenticated", true)
            );

        mockMvc
            .perform(get("/addComment?bookId=" + BOOK.getId()))
            .andExpect(status().isOk())
        ;
    }

    @DisplayName("запрещать комментировать книги сотрудникам")
    @WithMockUser(
        username = "admin",
        authorities = {"ROLE_ADMIN", "ROLE_MANAGER"}
    )
    @Test
    public void doNotAllowCommentForPrivilegedUsers() throws Exception {
        mockMvc
            .perform(get("/addComment?bookId=" + BOOK.getId()))
            .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value()))
        ;
    }

    private void assertContentForNonAuthenticatedUser(String responseContent) {
        assertBookInfo(responseContent);

        assertThat(responseContent).contains(LOGIN);
        assertThat(responseContent).contains(REGISTER);
        assertThat(responseContent.contains(EDIT)).isFalse();
        assertThat(responseContent.contains(REMOVE)).isFalse();
    }

    private void assertContentForCommonUser(String responseContent, AppPrincipal principal) {
        assertBookInfo(responseContent);

        assertThat(responseContent).doesNotContain(LOGIN);
        assertThat(responseContent).doesNotContain(REGISTER);
        assertThat(responseContent).contains(principal.fullName());
        assertThat(responseContent).contains(LOGOUT);
        assertThat(responseContent.contains(EDIT)).isFalse();
        assertThat(responseContent.contains(REMOVE)).isFalse();
    }

    private void assertContentForManager(String responseContent, AppPrincipal principal) {
        assertBookInfo(responseContent);

        assertThat(responseContent).contains(principal.fullName());
        assertThat(responseContent).contains(EDIT);
        assertThat(responseContent).contains(REMOVE);
        assertThat(responseContent).contains(LOGOUT);
    }

    private void assertBookInfo(String responseContent) {
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
