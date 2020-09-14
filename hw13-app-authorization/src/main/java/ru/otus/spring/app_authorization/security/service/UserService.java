package ru.otus.spring.app_authorization.security.service;

import ru.otus.spring.app_authorization.security.user.AppUser;
import ru.otus.spring.app_authorization.security.user.User;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

public interface UserService {
    ServiceResult<AppUser> add(User user);
}
