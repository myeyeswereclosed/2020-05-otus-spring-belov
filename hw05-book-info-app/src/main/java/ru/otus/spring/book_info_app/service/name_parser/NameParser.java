package ru.otus.spring.book_info_app.service.name_parser;

import ru.otus.spring.book_info_app.domain.Name;

public interface NameParser {
    Name parse(String input);
}
