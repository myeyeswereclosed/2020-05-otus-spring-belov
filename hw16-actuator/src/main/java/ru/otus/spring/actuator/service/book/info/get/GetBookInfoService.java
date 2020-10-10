package ru.otus.spring.actuator.service.book.info.get;

import ru.otus.spring.actuator.domain.BookInfo;
import ru.otus.spring.actuator.service.result.ServiceResult;

import java.util.List;

public interface GetBookInfoService {
    ServiceResult<BookInfo> get(String bookId);

    ServiceResult<List<BookInfo>> getAll();
}
