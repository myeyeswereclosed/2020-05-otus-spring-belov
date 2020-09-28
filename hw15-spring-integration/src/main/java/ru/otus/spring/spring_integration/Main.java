package ru.otus.spring.spring_integration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import ru.otus.spring.spring_integration.processing.Processing;

@IntegrationComponentScan
@ComponentScan
@Configuration
@EnableIntegration
public class Main {
    public static void main(String[] args) throws Exception {
        var context = new AnnotationConfigApplicationContext(Main.class);

        var processing = context.getBean(Processing.class);

        processing.run();
    }
}
