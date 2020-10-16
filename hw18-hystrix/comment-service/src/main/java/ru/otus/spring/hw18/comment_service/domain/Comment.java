package ru.otus.spring.hw18.comment_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import ru.otus.spring.hw18.lib.CommentDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;

    private String text;

    private String bookId;

    public static Comment fromDto(CommentDto dto) {
        return new Comment(dto.getId(), dto.getText(), dto.getBookId());
    }

    public Comment(String text) {
        this.text = text;
    }

    public Comment(String text, String bookId) {
        this.text = text;
        this.bookId = bookId;
    }

    public CommentDto toDto() {
        return new CommentDto(id, text, bookId);
    }
}
