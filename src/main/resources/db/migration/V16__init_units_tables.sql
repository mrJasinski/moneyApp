create table if not exists main_units
(
    id int primary key auto_increment,
    symbol varchar not null,
    name varchar not null
);

create table if not exists sub_units
(
    id int primary key auto_increment,
    multiplier int not null,
    prefix varchar not null,
    name varchar not null
);

create table if not exists units
(
    id int primary key auto_increment,
    main_unit_id int not null,
    sub_unit_id int not null
);

alter table units add foreign key (main_unit_id) references main_units(id);
alter table units add foreign key (sub_unit_id) references sub_units(id);