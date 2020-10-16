package ru.otus.spring.hw18.book_service.service.book.info.get;

import ru.otus.spring.hw18.book_service.domain.BookInfo;
import ru.otus.spring.hw18.book_service.service.result.ServiceResult;

import java.util.List;

public interface GetBookInfoService {
    ServiceResult<BookInfo> get(String bookId);
}
