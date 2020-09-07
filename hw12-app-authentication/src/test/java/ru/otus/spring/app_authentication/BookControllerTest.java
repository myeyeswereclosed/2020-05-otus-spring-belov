package ru.otus.spring.app_authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import ru.otus.spring.app_authentication.controller.BookController;
import ru.otus.spring.app_authentication.controller.auth_handler.AuthHandler;
import ru.otus.spring.app_authentication.controller.auth_handler.AuthHandlerImpl;
import ru.otus.spring.app_authentication.controller.error_handler.ErrorHandler;
import ru.otus.spring.app_authentication.security.user.AppPrincipal;
import ru.otus.spring.app_authentication.service.book.BookService;
import ru.otus.spring.app_authentication.service.result.Executed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен")
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookController controller;

    @MockBean
    private BookService bookService;

    @MockBean
    private ErrorHandler errorHandler;

    @MockBean
    private AuthHandler authHandler;

    @MockBean
    private UserDetailsService service;

    @Test
    public void permitAll() throws Exception {
        when(bookService.getAll()).thenReturn(new Executed<>(new ArrayList<>()));

        when(authHandler.authInfo(any(Model.class)))
            .thenAnswer(
                invocation ->
                    ((Model)invocation.getArguments()[0])
                        .addAttribute("user", "anonymousUser")
                        .addAttribute("isAnonymous", true)
            );

        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk())
        ;
    }

    @Test
    public void redirectToLoginPage() throws Exception {
        var paths = Arrays.asList("/addBook", "/editBook", "/addBookAuthor", "/addBookGenre");

        paths.forEach(
            path -> {
                try {
                    System.out.println("here");
                    mockMvc
                        .perform(get(path))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(result ->
                            assertThat(result.getResponse().getRedirectedUrl()).endsWith("/login")
                        )
                    ;
                } catch (Exception e) {
                    fail();
                }
            }
        );
    }
}
