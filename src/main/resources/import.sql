create sequence hibernate_sequence start 1 increment 1;

create table Customer (
   id int8 not null,
    description varchar(255),
    name varchar(255) not null,
    primary key (id)
);

alter table if exists Customer
   add constraint UK_6m4fuhxj4ckixi1vsb8osii7r unique (name);
