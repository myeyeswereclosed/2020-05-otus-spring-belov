package ru.otus.spring.actuator.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookInfoDto {
    private BookDto book;
    private List<String> comments;
}
