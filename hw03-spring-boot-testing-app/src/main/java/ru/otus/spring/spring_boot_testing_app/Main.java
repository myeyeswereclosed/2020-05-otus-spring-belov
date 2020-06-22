package ru.otus.spring.spring_boot_testing_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.spring.spring_boot_testing_app.config.Config;
import ru.otus.spring.spring_boot_testing_app.service.TestSystem;

@SpringBootApplication
@EnableConfigurationProperties(Config.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication
            .run(Main.class)
            .getBean(TestSystem.class)
            .run()
        ;
    }
}
