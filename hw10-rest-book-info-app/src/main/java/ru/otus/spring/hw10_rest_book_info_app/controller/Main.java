package ru.otus.spring.hw10_rest_book_info_app.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}
