package de.qaware.mercury.storage.reservation;

import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface ReservationRepository {
    void insert(Reservation reservation);

    List<Reservation> findReservationsForShop(Shop.Id shopId, LocalDateTime start, LocalDateTime end);

    @Nullable
    Reservation findById(Reservation.Id id);

    void deleteById(Reservation.Id id) throws ReservationNotFoundException;

    /**
     * Anonymizes all reservations that expired the day before.
     *
     * @param until   defines until which date and time the reservations should be anonymized.
     * @param updated current timestamp
     * @return the number of entries anonymized.
     */
    int anonymizeExpired(LocalDateTime until, ZonedDateTime updated);
}
