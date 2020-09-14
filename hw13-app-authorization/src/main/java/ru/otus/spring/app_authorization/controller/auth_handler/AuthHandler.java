package ru.otus.spring.app_authorization.controller.auth_handler;

import org.springframework.ui.Model;
import ru.otus.spring.app_authorization.security.user.AppPrincipal;

import java.util.Optional;

public interface AuthHandler {
    Model authInfo(Model model);

    Optional<AppPrincipal> principal();
}
