package de.qaware.mercury.business.reservation.impl;

import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.reservation.Interval;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationService;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.reservation.ReservationRepository;
import de.qaware.mercury.util.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class ReservationServiceImpl implements ReservationService {
    private final SlotService slotService;
    private final ReservationRepository reservationRepository;
    private final Clock clock;
    private final UUIDFactory uuidFactory;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<Slot> listSlots(Shop shop) {
        LocalDate today = clock.today();
        // Find all reservations in the time range
        List<Reservation> reservations = reservationRepository.findReservationsForShop(shop.getId(), today.atTime(0, 0), today.atTime(23, 59));
        // Now generate slots. The time ranges which also have reservations are marked as unavailable
        return slotService.generateSlots(today, today, shop.getSlotConfig(), mapReservations(reservations));
    }

    @Override
    @Transactional
    public void createReservation(Shop shop, Slot.Id slotId, ContactType contactType, String contact, String name, String email) {
        // TODO: Validate that slot is available
        // TODO: Validate if this is a valid slot

        Reservation.Id reservationId = Reservation.Id.random(uuidFactory);

        LocalDateTime start = slotId.toLocalDateTime();
        LocalDateTime end = start.plusMinutes(shop.getSlotConfig().getTimePerSlot());

        reservationRepository.insert(new Reservation(reservationId, shop.getId(), start, end, contact, email, contactType, clock.nowZoned(), clock.nowZoned()));

        log.info("Sending customer reservation confirmation to '{}'", email);
        emailService.sendCustomerReservationConfirmation(shop, email, name, start, end, contactType, contact, reservationId);
        log.info("Sending new shop reservation to '{}'", shop.getEmail());
        emailService.sendShopNewReservation(shop, name, start, end, contactType, contact, reservationId);
    }

    private List<Interval> mapReservations(List<Reservation> reservations) {
        return Lists.map(reservations, r -> Interval.of(r.getStart(), r.getEnd()));
    }
}
