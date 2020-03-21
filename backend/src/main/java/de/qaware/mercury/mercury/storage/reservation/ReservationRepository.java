package de.qaware.mercury.mercury.storage.reservation;

import de.qaware.mercury.mercury.business.reservation.Reservation;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.Slot;

import java.util.List;

public interface ReservationRepository {
    void insert(Reservation reservation);

    List<Reservation> findReservationsForShop(Shop.Id shopId);

    List<Slot> findSlotsForShop(Shop.Id shopId);
}
