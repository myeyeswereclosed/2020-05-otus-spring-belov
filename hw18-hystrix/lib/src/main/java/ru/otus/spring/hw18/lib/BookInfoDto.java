package ru.otus.spring.hw18.lib;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookInfoDto {
    private BookDto book;
    private List<String> comments;
}
