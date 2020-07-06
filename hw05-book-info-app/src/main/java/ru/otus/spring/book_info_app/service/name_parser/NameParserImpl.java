package ru.otus.spring.book_info_app.service.name_parser;

import org.springframework.stereotype.Component;
import ru.otus.spring.book_info_app.domain.Name;

@Component
public class NameParserImpl implements NameParser {
    @Override
    public Name parse(String input) {
        var nameEndsAt = input.indexOf(" ");

        var firstName = input.substring(0, nameEndsAt).trim();
        var lastName = input.substring(nameEndsAt + 1).trim();

        return new Name(firstName, lastName);
    }
}
