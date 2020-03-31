package de.qaware.mercury.storage.reservation.impl;

import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.storage.reservation.ReservationRepository;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaReservationRepositoryImpl implements ReservationRepository {

    private static final String ANONYMIZED_VALUE = "<anonymized>";

    private final ReservationDataRepository reservationDataRepository;
    private final Clock clock;

    @Override
    public void insert(Reservation reservation) {
        log.debug("Update {}", reservation);
        reservationDataRepository.save(ReservationEntity.of(reservation.withCreated(clock.nowZoned())));
    }

    @Override
    public List<Reservation> findReservationsForShop(Shop.Id shopId, LocalDateTime start, LocalDateTime end) {
        List<ReservationEntity> entities = reservationDataRepository.findReservationsForShop(shopId.getId(), start, end);

        return Lists.map(entities, ReservationEntity::toReservation);
    }

    @Override
    @Nullable
    public Reservation findById(Reservation.Id id) {
        ReservationEntity entity = reservationDataRepository.findById(id.getId()).orElse(null);

        return Null.map(entity, ReservationEntity::toReservation);
    }

    @Override
    public void deleteById(Reservation.Id id) throws ReservationNotFoundException {
        try {
            reservationDataRepository.deleteById(id.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new ReservationNotFoundException(id, e);
        }
    }

    @Override
    public int anonymizeExpired(LocalDateTime until, ZonedDateTime updated) {
        return reservationDataRepository.anonymizeExpired(until, ANONYMIZED_VALUE, updated);
    }
}
