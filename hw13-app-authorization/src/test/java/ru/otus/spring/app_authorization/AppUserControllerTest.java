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
import ru.otus.spring.app_authorization.controller.auth_handler.AuthHandlerImpl;
import ru.otus.spring.app_authorization.controller.book.BookController;
import ru.otus.spring.app_authorization.controller.error_handler.ErrorHandler;
import ru.otus.spring.app_authorization.controller.user.AppUserController;
import ru.otus.spring.app_authorization.security.service.UserService;
import ru.otus.spring.app_authorization.security.user.AppPrincipal;
import ru.otus.spring.app_authorization.security.user.AppUser;
import ru.otus.spring.app_authorization.security.user.Role;
import ru.otus.spring.app_authorization.security.user.User;
import ru.otus.spring.app_authorization.service.book.BookService;
import ru.otus.spring.app_authorization.service.result.Executed;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер пользователей приложения должен")
@WebMvcTest(AppUserController.class)
public class AppUserControllerTest {
    private static final AppPrincipal ADMIN =
        new AppPrincipal(
            "BookAppAdmin",
            "secret",
            Collections.singletonList(new SimpleGrantedAuthority(Role.ADMIN.toSecurityRole())),
            "I am",
            "Admin"
        );
    private static final User USER = new User("First Name", "Last Name", "SuperManager", "testpassword", Role.MANAGER);
    private static final AppUser APP_USER =
        new AppUser(
            "AppUserID",
            USER.getLogin(),
            "passwordEncrypted",
            USER.getFirstName(),
            USER.getLastName(),
            USER.getRole()
        );

    private static final String ADD_USER = "Add user:";
    private static final String ADD_BUTTON = "Add";
    private static final String BACK_BUTTON = "Back";
    private static final String REGISTER = "Register:";
    private static final String REGISTER_BUTTON = "Register:";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserController controller;

    @MockBean
    private UserService service;

    @MockBean
    private ErrorHandler errorHandler;

    @MockBean
    private AuthHandlerImpl authHandler;

    @MockBean
    private UserDetailsService userDetails;

    @DisplayName("позволять администратороам приложения добавлять пользователей в привилегиями")
    @WithMockUser(
        username = "admin",
        authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void addUserPageForAdmin() throws Exception {
        when(service.add(USER)).thenReturn(new Executed<>(APP_USER));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", ADMIN)
                        .addAttribute("isAuthenticated", true)
            );

        mockMvc
            .perform(get("/addUser"))
            .andExpect(status().isOk())
            .andExpect(result -> assertAddUserPage(result.getResponse().getContentAsString()))
        ;
    }

    @DisplayName("запрещать доступ к странице добавления пользователей не-администраторам приложения")
    @WithMockUser(
        username = "user",
        authorities = {"ROLE_USER", "ROLE_MANAGER"}
    )
    @Test
    public void addUserPageForNonAdmin() throws Exception {
        mockMvc
            .perform(get("/addUser"))
            .andExpect(
                result -> assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value())
            )
        ;
    }

    @DisplayName("запрещать доступ к странице добавления пользователей анонимным пользователям")
    @Test
    public void addUserPageForNonAuthenticated() throws Exception {
        mockMvc
            .perform(get("/addUser"))
            .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl()).endsWith("/login"))
        ;
    }

    @DisplayName("отдавать страницу регистрации анонимному пользователю")
    @Test
    public void registerAnonymousUser() throws Exception {
        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("isAuthenticated", false)
            );

        mockMvc
            .perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(result -> assertRegistrationPage(result.getResponse().getContentAsString()))
        ;
    }

    @DisplayName("запрещать доступ к странице регистрации авторизованным пользователям")
    @WithMockUser(
        username = "user",
        authorities = {"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"}
    )
    @Test
    public void doNotRegisterAuthorizedUsers() throws Exception {
        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("isAuthenticated", false)
            );

        mockMvc
            .perform(get("/register"))
            .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value()))
        ;
    }

    private void assertAddUserPage(String responseContent) {
        assertThat(responseContent).contains(ADD_USER);
        assertThat(responseContent).contains(ADD_BUTTON);
        assertThat(responseContent).contains(BACK_BUTTON);
    }

    private void assertRegistrationPage(String responseContent) {
        assertThat(responseContent).contains(REGISTER);
        assertThat(responseContent).contains(REGISTER_BUTTON);
    }
}
