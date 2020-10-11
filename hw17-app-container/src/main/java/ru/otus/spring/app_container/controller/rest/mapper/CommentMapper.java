package ru.otus.spring.app_container.controller.rest.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.app_container.controller.rest.dto.CommentDto;
import ru.otus.spring.app_container.domain.Comment;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CommentMapper implements DtoMapper<Comment, CommentDto> {
    private final BookMapper bookMapper;

    @Override
    public Comment toEntity(@NonNull CommentDto dto) {
        return
            Objects.nonNull(dto.getBook())
                ? new Comment(dto.getText(), bookMapper.toEntity(dto.getBook()))
                : new Comment(dto.getText())
            ;
    }

    @Override
    public CommentDto toDto(@NonNull Comment comment) {
        return
            new CommentDto(comment.getText(), bookMapper.toDto(comment.getBook()));
    }
}
