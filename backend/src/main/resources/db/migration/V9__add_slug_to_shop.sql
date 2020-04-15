-- Create a slug column and fill it with random data. the real slug will be generated in the V10 *java* migration!
ALTER TABLE shop
    ADD COLUMN slug varchar NOT NULL DEFAULT random()::text;
ALTER TABLE shop
    ALTER COLUMN slug DROP DEFAULT;

CREATE UNIQUE INDEX idx_shop_slug ON shop (slug);
