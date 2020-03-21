CREATE TABLE shop
(
    id            uuid primary key,
    name          text    not null,
    enabled       boolean not null,
    latitude      double  not null,
    longitude     double  not null
);