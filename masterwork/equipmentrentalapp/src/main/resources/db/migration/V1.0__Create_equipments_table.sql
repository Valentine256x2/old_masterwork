create table equipments
(
    id                  bigint generated by default as identity,
    category            varchar(255) not null,
    daily_price         bigint not null,
    name                varchar(255) not null,
    primary key (id)
);