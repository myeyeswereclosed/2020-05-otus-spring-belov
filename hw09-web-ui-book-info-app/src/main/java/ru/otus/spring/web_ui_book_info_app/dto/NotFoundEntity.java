package ru.otus.spring.web_ui_book_info_app.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class NotFoundEntity {
    private final String name;
    private final String id;
}
