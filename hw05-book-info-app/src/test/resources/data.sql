begin;

insert into book(title) values('Tri porosenka');
insert into author(first_name, last_name) values('Some', 'Author');
insert into genre(name) values('science'), ('history');

commit;