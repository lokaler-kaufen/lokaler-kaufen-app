CREATE TABLE shop
(
    id                 uuid primary key,
    name               varchar                  not null,
    owner_name         varchar                  not null,
    email              varchar                  not null,
    street             varchar                  not null,
    zip_code           varchar                  not null,
    city               varchar                  not null,
    address_supplement varchar                  not null,
    enabled            boolean                  not null,
    approved           boolean                  not null,
    whatsapp           varchar                  null,
    phone              varchar                  null,
    facetime           varchar                  null,
    google_duo         varchar                  null,
    skype              varchar                  null,
    signal             varchar                  null,
    viber              varchar                  null,
    latitude           double precision         not null,
    longitude          double precision         not null,
    details            varchar                  not null,
    website            varchar                  null,
    time_per_slot      int                      not null,
    time_between_slots int                      not null,
    monday_start       varchar                  null,
    monday_end         varchar                  null,
    tuesday_start      varchar                  null,
    tuesday_end        varchar                  null,
    wednesday_start    varchar                  null,
    wednesday_end      varchar                  null,
    thursday_start     varchar                  null,
    thursday_end       varchar                  null,
    friday_start       varchar                  null,
    friday_end         varchar                  null,
    saturday_start     varchar                  null,
    saturday_end       varchar                  null,
    sunday_start       varchar                  null,
    sunday_end         varchar                  null,
    created            timestamp with time zone not null,
    updated            timestamp with time zone not null
);
CREATE INDEX idx_shop_name ON shop (name);
CREATE INDEX idx_shop_details ON shop (details);
CREATE INDEX idx_shop_latitude ON shop (latitude);
CREATE INDEX idx_shop_longitude ON shop (longitude);

CREATE TABLE admin
(
    id            uuid primary key,
    email         varchar                  not null unique,
    password_hash varchar                  not null,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null
);
CREATE INDEX idx_admin_email ON admin (email);

CREATE TABLE shop_login
(
    id            uuid primary key,
    shop_id       uuid                     not null references shop (id) on delete cascade,
    email         varchar                  not null unique,
    password_hash varchar                  not null,
    created       timestamp with time zone not null,
    updated       timestamp with time zone not null
);
CREATE INDEX idx_shop_login_email ON shop_login (email);

CREATE TABLE reservation
(
    id           uuid primary key,
    shop_id      uuid                        not null references shop (id) on delete cascade,
    start_time   timestamp without time zone not null,
    end_time     timestamp without time zone not null,
    name         varchar                     not null,
    contact      varchar                     not null,
    email        varchar                     not null,
    contact_type varchar                     not null,
    anonymized   boolean                     not null,
    created      timestamp with time zone    not null,
    updated      timestamp with time zone    not null,
    unique (shop_id, start_time, end_time)
);
CREATE INDEX idx_reservation_shop_id ON reservation (shop_id);
CREATE INDEX idx_reservation_start_time ON reservation (start_time);
CREATE INDEX idx_reservation_end_time ON reservation (end_time);

-- schema derived from https://www.geonames.org/
CREATE TABLE geolocation
(
    id           bigint generated always as identity primary key,
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
CREATE INDEX idx_geolocation_zipcode ON geolocation (zip_code);
