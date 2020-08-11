package ru.otus.spring.spring_data_jpa_book_info_app.service.book;


import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.service.result.ServiceResult;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Book> rename(Book book);

    ServiceResult<Long> remove(long id);
}
