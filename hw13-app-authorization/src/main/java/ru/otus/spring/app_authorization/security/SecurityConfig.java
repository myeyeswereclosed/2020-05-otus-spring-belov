package ru.otus.spring.app_authorization.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.spring.app_authorization.security.user.Role;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final int ENCRYPTION_STRENGTH = 12;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/webjars/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .authorizeRequests()
            .antMatchers( "/", "/*.css", "/info", "/login" ).permitAll()
            .antMatchers("/register").anonymous()
            .antMatchers(HttpMethod.POST, "/register").anonymous()
            .antMatchers("/addUser").hasRole(Role.ADMIN.getName())
            .antMatchers("/addComment").hasAnyRole(Role.ANONYMOUS.getName(), Role.USER.getName())
            .antMatchers(HttpMethod.POST, "/addComment/*").hasAnyRole(Role.ANONYMOUS.getName(), Role.USER.getName())
            .antMatchers("/addBook", "/editBook", "/removeBook/*", "/addBookAuthor", "/addBookGenre").hasRole(Role.MANAGER.getName())
            .antMatchers(HttpMethod.POST, "/removeBook").hasRole(Role.MANAGER.getName())
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .defaultSuccessUrl("/")
                .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            .and()
            .exceptionHandling().accessDeniedPage("/forbidden")
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(ENCRYPTION_STRENGTH);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
        PasswordEncoder passwordEncoder,
        UserDetailsService userDetailsService
    ) {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }
}
