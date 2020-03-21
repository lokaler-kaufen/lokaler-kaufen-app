CREATE TABLE shop
(
    id                 uuid primary key,
    name               varchar not null,
    owner_name         text    not null,
    street             text    not null,
    zip_code           text    not null,
    city               text    not null,
    address_supplement text    not null,
    enabled            boolean not null,
    latitude           double  not null,
    longitude          double  not null
);

CREATE TABLE admin
(
    id            uuid primary key,
    email         varchar not null unique,
    password_hash varchar not null
);

CREATE TABLE shop_login
(
    id            uuid primary key,
    shop_id       uuid    not null references shop (id),
    email         varchar not null unique,
    password_hash varchar not null
);