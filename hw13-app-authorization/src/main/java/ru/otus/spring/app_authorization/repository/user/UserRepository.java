package ru.otus.spring.app_authorization.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.app_authorization.security.user.AppUser;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByLogin(String login);
}
