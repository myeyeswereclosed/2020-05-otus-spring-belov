package ru.otus.spring.app_authentication.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.otus.spring.app_authentication.repository.user.UserRepository;
import ru.otus.spring.app_authentication.security.user.AppPrincipal;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class UserDetailsDbService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return
            repository
                .findByLogin(login)
                .map(appUser ->
                    new AppPrincipal(
                        appUser.getLogin(),
                        appUser.getPasswordEncrypted(),
                        Collections.emptyList(),
                        appUser.getFirstName(),
                        appUser.getLastName()
                    )
                )
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + login + " not found"))
            ;
    }
}
