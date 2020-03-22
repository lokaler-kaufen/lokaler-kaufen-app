CREATE TYPE contact_type AS ENUM ('WHATSAPP','FACETIME','GOOGLE_DUO','SKYPE','SIGNAL_PRIVATE_MESSENGER','VIBER','PHONE');

CREATE TABLE shop
(
    id                 uuid primary key,
    name               varchar          not null,
    owner_name         varchar          not null,
    email              varchar          not null,
    street             varchar          not null,
    zip_code           varchar          not null,
    city               varchar          not null,
    address_supplement varchar          not null,
    contact_types      contact_type[]   not null,
    enabled            boolean          not null,
    latitude           double precision not null,
    longitude          double precision not null,
    details            varchar          not null,
    website            varchar,
    time_per_slot      int,
    time_between_slots int,
    monday_start       varchar,
    monday_end         varchar,
    tuesday_start      varchar,
    tuesday_end        varchar,
    wednesday_start    varchar,
    wednesday_end      varchar,
    thursday_start     varchar,
    thursday_end       varchar,
    friday_start       varchar,
    friday_end         varchar,
    saturday_start     varchar,
    saturday_end       varchar,
    sunday_start       varchar,
    sunday_end         varchar
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

CREATE TABLE reservation
(
    id                  uuid primary key,
    shop_id             uuid                     not null references shop (id),
    start_time          timestamp with time zone not null,
    end_time            timestamp with time zone not null,
    contact_information varchar                  not null,
    email               varchar                  not null,
    contact_type        contact_type             not null
);

-- schema derived from https://www.geonames.org/
CREATE TABLE geolocation
(
    country_code varchar          not null,
    zip_code     varchar          not null,
    place_name   varchar          not null,
    admin_name1  varchar          not null,
    admin_code1  varchar          not null,
    admin_name2  varchar          not null,
    admin_code2  varchar          not null,
    admin_name3  varchar          not null,
    admin_code3  varchar          not null,
    latitude     double precision not null,
    longitude    double precision not null,
    accuracy     int              not null
);