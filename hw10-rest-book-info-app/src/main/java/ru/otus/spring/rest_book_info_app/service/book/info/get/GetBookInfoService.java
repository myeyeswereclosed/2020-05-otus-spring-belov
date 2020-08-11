package ru.otus.spring.rest_book_info_app.service.book.info.get;

import ru.otus.spring.rest_book_info_app.domain.BookInfo;
import ru.otus.spring.rest_book_info_app.service.result.ServiceResult;

import java.util.List;

public interface GetBookInfoService {
    ServiceResult<BookInfo> get(String bookId);

    ServiceResult<List<BookInfo>> getAll();
}
