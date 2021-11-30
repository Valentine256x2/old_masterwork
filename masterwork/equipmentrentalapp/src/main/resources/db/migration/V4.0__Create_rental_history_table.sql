create table rental_history
(
    id                      bigint generated by default as identity,
    actual_return_date      date not null,
    expected_return_date    date not null,
    rental_date             date not null,
    user_id                 bigint not null,
    price_paid              bigint not null,
    primary key (id)
);
alter table rental_history add constraint FKruc22ceu963i7p4rot6ya46jh foreign key (user_id) references users;
