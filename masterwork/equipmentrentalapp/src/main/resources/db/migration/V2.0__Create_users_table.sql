create table users
(
    id              bigint generated by default as identity,
    address         varchar(255) not null,
    date_of_birth   date not null,
    email           varchar(255) not null,
    name            varchar(255) not null,
    primary key (id)
);
alter table users add constraint UniqueNameAndAddress unique (name, address);
alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
