package de.qaware.mercury.storage.reservation;

import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.shop.Shop;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    void insert(Reservation reservation);

    List<Reservation> findReservationsForShop(Shop.Id shopId, LocalDateTime start, LocalDateTime end);
}
