package ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.book;

import ru.otus.spring.mongo_db_book_info_app.dto.BookInfo;
import ru.otus.spring.mongo_db_book_info_app.service.shell.formatter.OperationResultFormatter;

import java.util.List;

public interface BookOperationFormatter extends OperationResultFormatter<BookInfo> {
    String listInfo(List<BookInfo> bookInfoList);
}
