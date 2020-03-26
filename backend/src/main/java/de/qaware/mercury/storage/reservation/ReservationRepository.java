package de.qaware.mercury.storage.reservation;

import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    void insert(Reservation reservation);

    List<Reservation> findReservationsForShop(Shop.Id shopId, LocalDateTime start, LocalDateTime end);

    @Nullable
    Reservation findById(Reservation.Id id);

    void deleteById(Reservation.Id id) throws ReservationNotFoundException;
}
