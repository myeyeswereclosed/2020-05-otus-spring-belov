package ru.otus.spring.app_authentication.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.app_authentication.security.user.AppUser;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByLogin(String login);
}
