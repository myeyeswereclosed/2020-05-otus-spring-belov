package ru.otus.spring.book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Author {
    private long id;
    private Name name;
}
