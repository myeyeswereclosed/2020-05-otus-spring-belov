package ru.otus.spring.spring_data_jpa_book_info_app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Book;
import ru.otus.spring.spring_data_jpa_book_info_app.domain.Comment;
import ru.otus.spring.spring_data_jpa_book_info_app.repository.comment.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий комментариев должен ")
@DataJpaTest
public class CommentRepositoryTest {
    private final static Book INITIAL_BOOK = new Book(1, "Tri porosenka");
    private final static Comment INITIAL_COMMENT = new Comment(1, "Good book!", INITIAL_BOOK);
    private final static Comment UPDATED_COMMENT = new Comment(1, "Super book!");

    @Autowired
    private CommentRepository repository;

    @DisplayName("находить все комментарии")
    @Test
    public void findAll() {
        var comments = repository.all();

        assertThat(comments.size()).isEqualTo(1);
        assertFirstComment(comments.get(0), INITIAL_COMMENT);
    }

    @DisplayName("находить cохраненный комментарий по идентификатору")
    @Test
    public void findById() {
        var comment = repository.findById(INITIAL_COMMENT.getId());

        assertFirstComment(comment.get(), INITIAL_COMMENT);
    }

    @DisplayName("отдавать пустой результат, если комментарий не найден")
    @Test
    public void emptyIfNotFound() {
        assertThat(repository.findById(0L)).isEmpty();
    }

    @DisplayName("обновлять текст комментария")
    @Test
    public void update() {
        repository.updateTextById(UPDATED_COMMENT.getId(), UPDATED_COMMENT.getText());

        var comments = repository.findAll();

        assertThat(comments.size()).isEqualTo(1);
        assertFirstComment(comments.get(0), UPDATED_COMMENT);
    }

    @DisplayName("удалять комментарий")
    @Test
    public void delete() {
        repository.deleteById(INITIAL_COMMENT.getId());

        assertThat(repository.findAll()).isEmpty();
    }

    private void assertFirstComment(Comment comment, Comment expectedComment) {
        assertThat(comment.getId()).isEqualTo(expectedComment.getId());
        assertThat(comment.getText()).isEqualTo(expectedComment.getText());
        assertThat(comment.getBook().getId()).isEqualTo(INITIAL_BOOK.getId());
        assertThat(comment.getBook().getTitle()).isEqualTo(INITIAL_BOOK.getTitle());
    }
}
