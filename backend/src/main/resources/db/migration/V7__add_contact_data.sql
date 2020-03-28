CREATE TABLE contact
(
    id           uuid primary key,
    shop_id      uuid    not null references shop (id),
    contact_type varchar not null,
    data         varchar not null
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO contact
SELECT uuid_generate_v4()                                                                        as id,
       id                                                                                        as shop_id,
       regexp_split_to_table(replace(replace(contact_types::text, '}', ''), '{', ''), ','::text) as contact_type,
       ''                                                                                        as data
FROM shop;