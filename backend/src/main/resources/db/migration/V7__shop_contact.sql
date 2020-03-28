CREATE TABLE shop_contact
(
    shop_id      uuid    not null references shop (id),
    contact_type varchar not null,
    data         varchar not null
);