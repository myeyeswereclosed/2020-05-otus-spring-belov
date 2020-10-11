package ru.otus.spring.app_container.service.book.info.get;

import ru.otus.spring.app_container.domain.BookInfo;
import ru.otus.spring.app_container.service.result.ServiceResult;

import java.util.List;

public interface GetBookInfoService {
    ServiceResult<BookInfo> get(String bookId);

    ServiceResult<List<BookInfo>> getAll();
}
