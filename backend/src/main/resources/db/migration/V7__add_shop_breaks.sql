CREATE TABLE shop_break
(
    id          uuid primary key,
    shop_id     uuid    not null references shop (id) on delete cascade,
    day_of_week int     not null, -- The values are numbered following the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
    break_start varchar not null,
    break_end   varchar not null
);
