use db;

SET FOREIGN_KEY_CHECKS=0; -- to disable them

drop table if exists users;
create table users(
    id int auto_increment primary key,
    first_name varchar(32),
    last_name varchar(32),
    phone_number varchar(32),
    username varchar(32),
    password varchar(32)
);

drop table if exists items;
create table items(
    id int auto_increment primary key,
    user_id int,
    name varchar(64),
    price int,
    description text,
    category varchar(32),
    constraint user_item_fk foreign key (user_id) references users (id)
);

drop table if exists wishlist;
create table wishlist(
    user_id int,
    item_id int,
    constraint wishlist_user_fk foreign key (user_id) references users (id),
    constraint wishlist_item_fk foreign key (item_id) references items (id)
);

SET FOREIGN_KEY_CHECKS=1; -- to re-enable them