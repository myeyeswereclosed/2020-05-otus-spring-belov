package ru.otus.spring.reactive_book_info_app.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenreDto {
    private String id;
    private String name;
}
