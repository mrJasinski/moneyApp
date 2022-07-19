create table if not exists brands
(
    id int primary key auto_increment,
    name varchar not null,
    hash int not null
);

create table if not exists products
(
    id int primary key auto_increment,
    brand_id int not null,
    genre_id int not null,
    name varchar,
    bar_code varchar,
    quantity float not null,
    unit_id int not null ,
    description varchar,
    hash int not null
);

alter table products add foreign key (brand_id) references brands (id);
alter table products add foreign key (genre_id) references genres (id);
alter table products add foreign key (unit_id) references units (id);