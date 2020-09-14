package ru.otus.spring.app_authorization.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.spring.app_authorization.infrastructure.AppLogger;
import ru.otus.spring.app_authorization.infrastructure.AppLoggerFactory;
import ru.otus.spring.app_authorization.repository.user.UserRepository;
import ru.otus.spring.app_authorization.security.user.AppUser;
import ru.otus.spring.app_authorization.security.user.User;
import ru.otus.spring.app_authorization.service.result.Executed;
import ru.otus.spring.app_authorization.service.result.Failed;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final AppLogger logger = AppLoggerFactory.logger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    @Override
    public ServiceResult<AppUser> add(User user) {
        try {
            return
                new Executed<>(
                    repository.save(
                        new AppUser(
                            user.getLogin(),
                            passwordEncoder.encode(user.getPassword()),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getRole()
                        )
                    )
                );
        } catch (Exception e) {
            logger.logException(e);
        }

        return new Failed<>();
    }
}
