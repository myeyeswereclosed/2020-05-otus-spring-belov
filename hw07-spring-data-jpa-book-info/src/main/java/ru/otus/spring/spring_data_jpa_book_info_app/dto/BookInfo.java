package ru.otus.spring.spring_data_jpa_book_info_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;

import java.util.Set;

@AllArgsConstructor
@Data
public class BookInfo {
    private Book book;
    private Set<Comment> comments;

    public long bookId() {
        return book.getId();
    }
}
