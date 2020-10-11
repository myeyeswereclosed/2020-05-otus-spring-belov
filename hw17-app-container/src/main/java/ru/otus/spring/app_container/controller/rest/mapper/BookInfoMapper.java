package ru.otus.spring.app_container.controller.rest.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.app_container.controller.rest.dto.BookInfoDto;
import ru.otus.spring.app_container.domain.BookInfo;
import ru.otus.spring.app_container.domain.Comment;

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
