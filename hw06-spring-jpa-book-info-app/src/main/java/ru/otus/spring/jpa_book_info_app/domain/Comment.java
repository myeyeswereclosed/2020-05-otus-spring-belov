package ru.otus.spring.jpa_book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    private Book book;

    public Comment(String text) {
        this.text = text;
    }

    public Comment(long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return
            new StringBuilder("id=").append(id)
                .append(";text = '").append(text).append("'")
                .append(";book = '").append(book.getTitle()).append("'")
                .toString();
    }

    public boolean hasNoId() {
        return id == 0;
    }
}
