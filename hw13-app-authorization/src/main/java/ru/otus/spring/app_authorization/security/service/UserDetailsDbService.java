package ru.otus.spring.app_authorization.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.spring.app_authorization.repository.user.UserRepository;
import ru.otus.spring.app_authorization.security.user.AppPrincipal;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDetailsDbService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return
            repository
                .findByLogin(login)
                .map(
                    appUser ->
                        new AppPrincipal(
                            appUser.getLogin(),
                            appUser.getPasswordEncrypted(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + appUser.getRole())),
                            appUser.getFirstName(),
                            appUser.getLastName()
                        )
                )
                .orElseThrow(() ->{
                    throw new UsernameNotFoundException("User with login " + login + " not found");
                })
            ;
    }
}
