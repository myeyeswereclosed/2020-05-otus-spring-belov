package ru.otus.spring.reactive_book_info_app.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDto {
    private String id;
    private String firstName;
    private String lastName;
}
