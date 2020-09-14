package ru.otus.spring.app_authorization.controller.auth_handler;

import org.springframework.ui.Model;

public interface AuthHandler {
    Model authInfo(Model model);
}
