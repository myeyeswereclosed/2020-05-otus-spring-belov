package ru.otus.spring.app_authorization.service.book.info.get;

import ru.otus.spring.app_authorization.dto.BookInfo;
import ru.otus.spring.app_authorization.service.result.ServiceResult;

import java.util.List;

public interface GetBookInfoService {
    ServiceResult<BookInfo> get(String bookId);

    ServiceResult<List<BookInfo>> getAll();
}
