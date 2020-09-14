package ru.otus.spring.app_authorization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.app_authorization.controller.auth_handler.AuthHandler;
import ru.otus.spring.app_authorization.security.user.User;

@RequiredArgsConstructor
@Controller
public class SecurityController {
    private final AuthHandler authHandler;

    @GetMapping("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
