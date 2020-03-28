CREATE INDEX idx_reservation_shopid_starttime_endtime ON reservation (shop_id, start_time, end_time);

CREATE INDEX idx_shop_name_details_enabled ON shop (enabled, approved, lower(name), lower(details));