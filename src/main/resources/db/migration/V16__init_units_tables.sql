create table if not exists main_units
(
    id int primary key,
    symbol varchar,
    name varchar
)
as select * from CSVREAD('./data/mainUnits.csv');

create table if not exists sub_units
(
    id int primary key,
    multiplier int,
    prefix varchar,
    name varchar
)
as select * from CSVREAD('./data/subUnits.csv');

create table if not exists units
(
    id int primary key,
    main_unit_id int,
    sub_unit_id int
)
as select * from CSVREAD('./data/units.csv');;

alter table units add foreign key (main_unit_id) references main_units(id);
alter table units add foreign key (sub_unit_id) references sub_units(id);