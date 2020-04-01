ALTER TABLE admin
    ADD COLUMN email_on_shop_approval_needed boolean NOT NULL DEFAULT true;
ALTER TABLE admin
    ALTER COLUMN email_on_shop_approval_needed DROP DEFAULT;
