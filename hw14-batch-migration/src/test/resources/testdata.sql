begin;

insert into book(title) values
    ('1 v pole ne voin'),
    ('2 kota'),
    ('3 porosenka'),
    ('4 skazki'),
    ('5 ozer');

insert into author(first_name, last_name) values
    ('Alexander', 'Pushkin'),
    ('Michail', 'Lermontov'),
    ('Unknown', 'Author');

insert into book_author(book_id, author_id) values
    (2,1), (2,2), (3,2), (4,1), (5,1), (5,2);

insert into genre(name) values('horror'), ('unused'), ('animals'), ('fairy-tail');

insert into book_genre(book_id, genre_id) values
    (2,1), (3,1), (3,3), (4,4), (5,1), (5,3), (5,4);

insert into comment(text, book_id) values
    ('O kotah', 2),
    ('Good book!', 2),
    ('Super book!', 4)
;

commit;