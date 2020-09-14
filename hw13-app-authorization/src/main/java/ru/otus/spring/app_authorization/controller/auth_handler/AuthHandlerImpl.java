package ru.otus.spring.app_authorization.controller.auth_handler;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.otus.spring.app_authorization.security.user.AppPrincipal;

import java.util.Objects;

@Component
public class AuthHandlerImpl implements AuthHandler {
    public Model authInfo(Model model) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        return
            model
                .addAttribute("user", authentication.getPrincipal())
                .addAttribute("isAuthenticated", !isAnonymous(authentication))
                .addAttribute("canManage", canManage(authentication))
                .addAttribute("canComment", canComment(authentication))
        ;
    }

    private boolean canManage(Authentication authentication) {
        return
            !isAnonymous(authentication)
                &&
            ((AppPrincipal)authentication.getPrincipal()).isManager()
        ;
    }

    private boolean canComment(Authentication authentication) {
        return
            isAnonymous(authentication)
                ||
            ((AppPrincipal)authentication.getPrincipal()).isUser()
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
