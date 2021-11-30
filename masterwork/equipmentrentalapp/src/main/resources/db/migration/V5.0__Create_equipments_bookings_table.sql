create table equipments_bookings
(
    booking_id      bigint not null,
    equipment_id    bigint not null,
    primary key (booking_id, equipment_id)
);
alter table equipments_bookings add constraint FKoo024m4hweng7odthaxnvnmwn foreign key (equipment_id) references equipments;
alter table equipments_bookings add constraint FKlrbt7cvjeo7p4uxhuy1pwn31x foreign key (booking_id) references bookings;
