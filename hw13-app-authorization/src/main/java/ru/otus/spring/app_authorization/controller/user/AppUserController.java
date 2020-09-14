package ru.otus.spring.app_authorization.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.app_authorization.controller.auth_handler.AuthHandler;
import ru.otus.spring.app_authorization.controller.error_handler.ErrorHandler;
import ru.otus.spring.app_authorization.security.service.UserService;
import ru.otus.spring.app_authorization.security.user.User;

@RequiredArgsConstructor
@Controller
public class AppUserController {
    private final static String HOME = "redirect:/";
    private final static String ERROR_TEMPLATE = "error";

    private final AuthHandler authHandler;
    private final ErrorHandler errorHandler;
    private final UserService service;

    @GetMapping("/addUser")
    public String addUserPage(Model model) {
        authHandler
            .authInfo(model)
            .addAttribute("appUser", new User())
        ;

        return "user/add";
    }

    @PostMapping("/addUser")
    public String add(User user, BindingResult bindingResult) {
        return
            errorHandler
                .handle(bindingResult, ERROR_TEMPLATE)
                .orElseGet(
                    () -> {
                        var serviceResult = service.add(user);

                        return
                            errorHandler
                                .handle(serviceResult, ERROR_TEMPLATE)
                                .orElse(
                                    serviceResult
                                        .value()
                                        .map(userAdded -> HOME)
                                        .orElse(ERROR_TEMPLATE)
                                );
                    }
                );
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        authHandler
            .authInfo(model)
            .addAttribute("appUser", new User());

        return "user/register";
    }

    @PostMapping("/register")
    public String register(User user, BindingResult bindingResult) {
        return add(user, bindingResult);
    }
}
