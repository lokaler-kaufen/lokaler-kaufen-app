DROP INDEX idx_reservation_shopid_starttime_endtime;
CREATE UNIQUE INDEX idx_reservation_shopid_starttime_endtime ON reservation (shop_id, start_time, end_time);