package ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.book;


import ru.otus.spring.hw07_apring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.hw07_apring_data_jpa_book_info_app.service.result.ServiceResult;

public interface BookService {
    ServiceResult<Book> addBook(Book book);

    ServiceResult<Void> rename(Book book);

    ServiceResult<Void> remove(long id);
}
