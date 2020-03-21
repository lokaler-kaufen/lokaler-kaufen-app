CREATE TABLE shop
(
    id        uuid primary key,
    name      varchar not null,
    enabled   boolean not null,
    latitude  double  not null,
    longitude double  not null
);

CREATE TABLE admin
(
    id            uuid primary key,
    email         varchar not null unique,
    password_hash varchar not null
);