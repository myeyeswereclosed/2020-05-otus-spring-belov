package ru.otus.spring.app_container.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
    private String id;
    private String title;
    private List<AuthorDto> authors = new ArrayList<>();
    private List<GenreDto> genres = new ArrayList<>();
}
