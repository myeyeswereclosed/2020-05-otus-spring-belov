package ru.otus.spring.jpa_book_info_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "book")
public class Book {

    public Book(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Book(String title) {
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "book_genre",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, insertable=false, updatable=false)
    private Set<Comment> comments = new HashSet<>();

    public boolean hasNoId() {
        return id == 0;
    }

    public Book addAuthor(Author author) {
        addToSet(authors, author);

        return this;
    }

    public boolean isWrittenBy(Author author) {
        return
            authors
                .parallelStream()
                .anyMatch(existingOne -> existingOne.hasFirstAndLastName(author.getFirstName(), author.getLastName()))
            ;
    }

    public Book addGenre(Genre genre) {
        return addToSet(genres, genre);
    }

    public boolean hasGenre(Genre genre) {
        return genres.parallelStream().anyMatch(existingOne -> existingOne.hasName(genre.getName()));
    }

    public Book addComment(Comment comment) {
        addToSet(comments, comment);
        comment.setBook(this);

        return this;
    }

//    @Override
//    public String toString() {
//        return
//            new StringBuilder("Book: {id = ").append(id)
//                .append(", title = ").append(title)
//                .append(", authors = ").append(authors)
//                .append(", genres = ").append(genres)
//                .append(", comments = ").append(comments)
//                .append('}')
//                .toString()
//        ;
//    }

    private<T> Book addToSet(Set<T> set, T newItem) {
        if (Objects.nonNull(newItem)) {
            set.add(newItem);
        }

        return this;
    }

}