package ru.otus.spring.hw18.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BookDto {
    private String id;
    private String title;
    private List<AuthorDto> authors = new ArrayList<>();
    private List<GenreDto> genres = new ArrayList<>();
}
