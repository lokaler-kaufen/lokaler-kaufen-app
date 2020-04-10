ALTER TABLE shop
    ADD COLUMN delay_for_first_slot int NOT NULL DEFAULT 0;
ALTER TABLE shop
    ALTER COLUMN delay_for_first_slot DROP DEFAULT;
