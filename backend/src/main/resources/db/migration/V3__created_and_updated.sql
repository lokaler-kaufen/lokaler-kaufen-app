ALTER TABLE shop
    ADD COLUMN created timestamp with time zone NOT NULL DEFAULT now(),
    ADD COLUMN updated timestamp with time zone NOT NULL DEFAULT now();

ALTER TABLE reservation
    ADD COLUMN created timestamp with time zone NOT NULL DEFAULT now(),
    ADD COLUMN updated timestamp with time zone NOT NULL DEFAULT now();

ALTER TABLE shop_login
    ADD COLUMN created timestamp with time zone NOT NULL DEFAULT now(),
    ADD COLUMN updated timestamp with time zone NOT NULL DEFAULT now();

ALTER TABLE admin
    ADD COLUMN created timestamp with time zone NOT NULL DEFAULT now(),
    ADD COLUMN updated timestamp with time zone NOT NULL DEFAULT now();
