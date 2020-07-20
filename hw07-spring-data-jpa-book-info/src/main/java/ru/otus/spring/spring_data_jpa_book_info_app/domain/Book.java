package ru.otus.spring.spring_data_jpa_book_info_app.domain;

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

    private<T> Book addToSet(Set<T> set, T newItem) {
        if (Objects.nonNull(newItem)) {
            set.add(newItem);
        }

        return this;
    }

}
