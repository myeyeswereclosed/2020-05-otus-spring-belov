package ru.otus.spring.book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Genre {
    private int id;
    private String name;
}
