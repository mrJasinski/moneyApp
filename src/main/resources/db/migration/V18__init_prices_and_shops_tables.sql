create table if not exists shops
(
    id int primary key auto_increment,
    name varchar not null,
    hash int
);

create table if not exists prices
(
    id int primary key auto_increment,
    product_id int not null,
    shop_id int not null,
    price float not null
);

alter table prices add foreign key (product_id) references PRODUCTS (id);
alter table prices add foreign key (shop_id) references shops (id);