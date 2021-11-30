create table equipments_rental_history
(
    rental_history_id   bigint not null,
    equipment_id        bigint not null,
    primary key (rental_history_id, equipment_id)
);
alter table equipments_rental_history add constraint FKsc37ofrhlvtbqpfcalmer1qkj foreign key (equipment_id) references equipments;
alter table equipments_rental_history add constraint FKvfvktp7w7fxt97b1jh8vaqdv foreign key (rental_history_id) references rental_history;
