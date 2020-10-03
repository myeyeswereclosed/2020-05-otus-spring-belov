begin;

insert into book(title) values ('Tri porosenka'), ('Tri losenka'), ('Tri sovenka');
insert into author(first_name, last_name) values('Some', 'Author'), ('Another', 'One');
insert into book_author(book_id, author_id) values (1, 1), (2, 1), (2,2);

insert into genre(name) values('science'), ('history');
insert into book_genre(book_id, genre_id) values (1, 1), (2, 1), (2,2);

insert into comment(book_id, text) values
    (1, 'Good book!'), (1, 'Super!'), (3, 'About birds)');

-- some load to check it's ok to migrate authors in one batch)
insert into author(first_name, last_name)
    select 'FirstName_' || x.id, 'LastName_' || x.id
    from generate_series(1, 100000) as x(id);

commit;

