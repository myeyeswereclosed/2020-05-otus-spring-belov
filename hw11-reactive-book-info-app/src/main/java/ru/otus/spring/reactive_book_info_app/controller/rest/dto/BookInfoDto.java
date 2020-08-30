package ru.otus.spring.reactive_book_info_app.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookInfoDto {
    private BookDto book;
    private List<String> comments;
}
