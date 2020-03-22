package de.qaware.mercury.mercury.storage.reservation.impl;

import de.qaware.mercury.mercury.business.reservation.Reservation;
import de.qaware.mercury.mercury.business.shop.DayConfig;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.storage.reservation.ReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaReservationRepositoryImpl implements ReservationRepository {
    private final ReservationDataRepository reservationDataRepository;
    private final EntityManager entityManager;

    @Override
    public void insert(Reservation reservation) {
        log.debug("Update {}", reservation);
        reservationDataRepository.save(ReservationEntity.of(reservation));
    }

    @Override
    public List<Reservation> findReservationsForShop(Shop.Id shopId) {
        // TODO: Implement
        return new ArrayList<>();
    }

    @Override
    public List<DayConfig> findSlotsForShop(Shop.Id shopId) {
        // TODO: Implement
        return new ArrayList<>();
    }
}
