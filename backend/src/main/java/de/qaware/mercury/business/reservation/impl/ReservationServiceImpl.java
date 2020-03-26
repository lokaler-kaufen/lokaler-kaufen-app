package de.qaware.mercury.business.reservation.impl;

import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.ReservationCancellationToken;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.reservation.Interval;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationCancellation;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.reservation.ReservationService;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
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
    private final TokenService tokenService;
    private final ShopService shopService;

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

        reservationRepository.insert(new Reservation(reservationId, shop.getId(), start, end, contact, email, name, contactType, clock.nowZoned(), clock.nowZoned()));

        log.info("Sending customer reservation confirmation to '{}'", email);
        emailService.sendCustomerReservationConfirmation(shop, email, name, start, end, contactType, contact, reservationId);
        log.info("Sending new shop reservation to '{}'", shop.getEmail());
        emailService.sendShopNewReservation(shop, name, start, end, contactType, contact, reservationId);
    }

    @Override
    @Transactional
    public void cancelReservation(ReservationCancellationToken token) throws ReservationNotFoundException, LoginException, ShopNotFoundException {
        ReservationCancellation cancellation = tokenService.verifyReservationCancellationToken(token);

        Reservation reservation = reservationRepository.findById(cancellation.getReservationId());
        if (reservation == null) {
            throw new ReservationNotFoundException(cancellation.getReservationId());
        }
        Shop shop = shopService.findByIdOrThrow(reservation.getShopId());
        reservationRepository.deleteById(reservation.getId());

        switch (cancellation.getSide()) {
            case SHOP:
                // Shop cancelled, send email to customer
                log.info("Sending reservation cancellation to customer '{}'", reservation.getEmail());
                emailService.sendReservationCancellationToCustomer(shop, reservation);
                // Send cancellation confirmation to shop
                emailService.sendReservationCancellationConfirmation(shop.getEmail(), reservation);
                break;
            case CUSTOMER:
                // Customer cancelled, send email to shop
                log.info("Sending reservation cancellation to shop '{}'", shop.getEmail());
                emailService.sendReservationCancellationToShop(shop, reservation);
                // Send cancellation confirmation to customer
                emailService.sendReservationCancellationConfirmation(reservation.getEmail(), reservation);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cancellation.getSide());
        }
    }

    private List<Interval> mapReservations(List<Reservation> reservations) {
        return Lists.map(reservations, r -> Interval.of(r.getStart(), r.getEnd()));
    }
}
