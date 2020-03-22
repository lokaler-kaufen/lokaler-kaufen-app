package de.qaware.mercury.mercury.storage.reservation;

import de.qaware.mercury.mercury.business.reservation.Reservation;
import de.qaware.mercury.mercury.business.shop.DayConfig;
import de.qaware.mercury.mercury.business.shop.Shop;

import java.util.List;

public interface ReservationRepository {
    void insert(Reservation reservation);

    List<Reservation> findReservationsForShop(Shop.Id shopId);

    List<DayConfig> findSlotsForShop(Shop.Id shopId);
}
