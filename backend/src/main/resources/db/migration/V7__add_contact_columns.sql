ALTER TABLE shop
    ADD COLUMN whatsapp varchar null;

ALTER TABLE shop
    ADD COLUMN phone varchar null;

ALTER TABLE shop
    ADD COLUMN facetime varchar null;

ALTER TABLE shop
    ADD COLUMN google_duo varchar null;

ALTER TABLE shop
    ADD COLUMN skype varchar null;

ALTER TABLE shop
    ADD COLUMN signal varchar null;

ALTER TABLE shop
    ADD COLUMN viber varchar null;

UPDATE shop
SET whatsapp = ''
WHERE contact_types @> ARRAY ['WHATSAPP']::varchar[];

UPDATE shop
SET PHONE = ''
WHERE contact_types @> ARRAY ['PHONE']::varchar[];

UPDATE shop
SET FACETIME = ''
WHERE contact_types @> ARRAY ['FACETIME']::varchar[];

UPDATE shop
SET GOOGLE_DUO = ''
WHERE contact_types @> ARRAY ['GOOGLE_DUO']::varchar[];

UPDATE shop
SET SKYPE = ''
WHERE contact_types @> ARRAY ['SKYPE']::varchar[];

UPDATE shop
SET SIGNAL = ''
WHERE contact_types @> ARRAY ['SIGNAL']::varchar[];

ALTER TABLE shop
    DROP COLUMN contact_types;