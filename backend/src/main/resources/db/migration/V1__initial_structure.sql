CREATE TYPE contact_type AS ENUM ('facebook_messenger', 'glide', 'google_duo', 'whatsapp', 'skype', 'justalk', 'signal_private_messenger', 'snapchat', 'tango', 'viber', 'telefon');

CREATE TABLE shop
(
    id                 uuid primary key,
    name               varchar          not null,
    owner_name         varchar          not null,
    street             varchar          not null,
    zip_code           varchar          not null,
    city               varchar          not null,
    address_supplement varchar          not null,
    contact_type       contact_type[]   not null,
    enabled            boolean          not null,
    latitude           double precision not null,
    longitude          double precision not null
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