package ru.otus.spring.book_info_app.service.book_info;

import ru.otus.spring.book_info_app.domain.BookInfo;
import ru.otus.spring.book_info_app.domain.Name;
import ru.otus.spring.book_info_app.service.result.ServiceResult;

public interface BookInfoService {
    ServiceResult<Void> addAuthor(long bookId, Name name);

    ServiceResult<Void> addGenre(long bookId, String name);

    ServiceResult<BookInfo> get(long bookId);
}
