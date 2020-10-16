package ru.otus.spring.hw18.book_info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BookInfo {
    public static void main(String[] args) {
        SpringApplication.run(BookInfo.class);
    }
}
