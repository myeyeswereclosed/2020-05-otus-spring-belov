begin;

drop table if exists author cascade;
drop table if exists book cascade;
drop table if exists genre cascade;
drop table if exists book_author;
drop table if exists book_genre;

create table author(
    id bigserial primary key,
    first_name varchar(63) not null,
    last_name varchar(255) not null,
    constraint uq__author_first_name_last_name unique(first_name, last_name)
);

create table book(id bigserial primary key, title varchar(255) not null);

create table genre(id serial primary key, name varchar(63) not null unique);

create table book_author(
    id bigserial primary key,
    book_id bigint not null,
    author_id bigint not null,
    constraint fk__book_author__book_id foreign key(book_id)
        references book(id) on update cascade on delete cascade,
    constraint fk__book_author__author_id foreign key(author_id)
        references author(id) on update cascade on delete cascade,
    constraint uq__book_author_book_id_author_id unique(book_id, author_id)
);

create table book_genre(
    id bigserial primary key,
    book_id bigint not null,
    genre_id bigint not null,
    constraint fk__book_genre__book_id foreign key(book_id)
        references book(id) on update cascade on delete cascade,
    constraint fk__book_genre__genre_id foreign key(genre_id)
        references genre(id) on update cascade on delete cascade,
    constraint uq__book_genre_book_id_genre_id unique(book_id, genre_id)
);

commit;