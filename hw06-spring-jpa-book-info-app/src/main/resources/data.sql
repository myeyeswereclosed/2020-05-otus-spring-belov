begin;

insert into book(title) values('Tri porosenka');
insert into author(first_name, last_name) values('Some', 'Author');
insert into book_author(book_id, author_id) values (1, 1);
insert into genre(name) values('horror'), ('history');
insert into book_genre(book_id, genre_id) values(1, 1);
insert into comment(text, book_id) values('Good book!', 1);

commit;