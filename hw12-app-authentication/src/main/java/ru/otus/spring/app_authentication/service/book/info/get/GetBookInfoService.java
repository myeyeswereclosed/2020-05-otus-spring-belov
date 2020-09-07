package ru.otus.spring.app_authentication.service.book.info.get;

import ru.otus.spring.app_authentication.dto.BookInfo;
import ru.otus.spring.app_authentication.service.result.ServiceResult;

import java.util.List;

public interface GetBookInfoService {
    ServiceResult<BookInfo> get(String bookId);

    ServiceResult<List<BookInfo>> getAll();
}
