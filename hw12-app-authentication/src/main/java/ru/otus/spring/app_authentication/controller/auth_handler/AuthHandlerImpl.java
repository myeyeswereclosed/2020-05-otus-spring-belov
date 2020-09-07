package ru.otus.spring.app_authentication.controller.auth_handler;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Objects;

@Component
public class AuthHandlerImpl implements AuthHandler {
    public Model authInfo(Model model) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        return
            model
                .addAttribute("user", authentication.getPrincipal())
                .addAttribute("isAnonymous", isAnonymous(authentication))
        ;
    }

    private boolean isAnonymous(Authentication authentication) {
        return
            Objects.isNull(authentication)
                ||
            !authentication.isAuthenticated()
                ||
            authentication instanceof AnonymousAuthenticationToken
        ;
    }
}
