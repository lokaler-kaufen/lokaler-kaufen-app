CREATE TABLE shop_contact
(
    id           uuid primary key,
    shop_id      uuid    not null references shop (id),
    contact_type varchar not null,
    data         varchar not null
)