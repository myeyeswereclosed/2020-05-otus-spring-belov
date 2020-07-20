package ru.otus.spring.jpa_book_info_app.dto;

public interface BookAuthor {
    long getAuthorId();

    String getAuthorFirstName();

    String getAuthorLastName();

    long getBookId();
}
