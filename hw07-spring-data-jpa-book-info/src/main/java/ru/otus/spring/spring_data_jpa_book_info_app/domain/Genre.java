package ru.otus.spring.spring_data_jpa_book_info_app.domain;

import lombok.*;
import ru.otus.spring.spring_data_jpa_book_info_app.dto.BookGenre;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
@Data
@Entity
@SqlResultSetMapping(
    name = "BookGenreMapping",
    classes =
    @ConstructorResult(
        targetClass = BookGenre.class,
        columns = {
            @ColumnResult(name = "genre_id", type = Integer.class),
            @ColumnResult(name = "name"),
            @ColumnResult(name = "book_id", type = Long.class)
        }
    )
)
@NamedNativeQuery(
    name="bookGenres",
    query=
        "select g.id genre_id, g.name, bg.book_id book_id " +
        "from genre g join book_genre bg " +
        "on bg.genre_id = g.id",
    resultSetMapping="BookGenreMapping"
)
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(
//        name = "book_genre",
//        joinColumns = @JoinColumn(name = "genre_id"),
//        inverseJoinColumns = @JoinColumn(name = "book_id")
//    )
//    private Set<Book> books = new HashSet<>();

    public static Genre fromDto(BookGenre bookGenre) {
        return new Genre(bookGenre.getGenreId(), bookGenre.getGenreName());
    }

    public Genre(String name) {
        this.name = name;
    }

//    public Genre(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }

    public boolean hasNoId() {
        return id == 0;
    }

//    @Override
//    public boolean equals(Object object) {
//        return
//            Objects.nonNull(object)
//                &&
//            object instanceof Genre
//                &&
//            name.equals(((Genre)object).getName())
//        ;
//    }

    public boolean hasName(String name) {
        return Objects.nonNull(this.name) && this.name.equals(name);
    }
}
