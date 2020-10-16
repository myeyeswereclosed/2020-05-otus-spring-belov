package ru.otus.spring.hw18.book_service.controller.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.hw18.book_service.domain.BookInfo;
import ru.otus.spring.hw18.book_service.domain.Comment;
import ru.otus.spring.hw18.lib.BookInfoDto;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class BookInfoMapper implements DtoMapper<BookInfo, BookInfoDto> {
    private final BookMapper bookMapper;

    @Override
    public BookInfo toEntity(@NonNull BookInfoDto dto) {
        return
            new BookInfo(
                bookMapper.toEntity(dto.getBook()),
                dto.getComments().stream().map(Comment::new).collect(toList())
            );
    }

    @Override
    public BookInfoDto toDto(@NonNull BookInfo bookInfo) {
        return
            new BookInfoDto(
                bookMapper.toDto(bookInfo.getBook()),
                bookInfo.getComments().stream().map(Comment::getText).collect(toList())
            );
    }
}
