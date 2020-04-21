package de.qaware.mercury.business.reservation.impl;

import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.ReservationCancellationToken;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.reservation.Interval;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationCancellation;
import de.qaware.mercury.business.reservation.ReservationFailedException;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.reservation.ReservationService;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.reservation.Slots;
import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.reservation.ReservationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    public Slots listSlots(Shop shop, int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("days must not be <= 0, was " + days);
        }

        LocalDate begin = clock.today();
        // if passed day = 1, you'll only get today
        // if passed day = 2, you'll get today and tomorrow
        LocalDate end = begin.plusDays(days - 1L);

        // Find all reservations in the time range
        List<Reservation> reservations = reservationRepository.findReservationsForShop(shop.getId(), begin.atTime(0, 0), end.atTime(23, 59));
        // Find all breaks in the time range
        Breaks breaks = shopService.findBreaks(shop);

        List<Interval> blockedSlots = blockSlots(begin, end, reservations, breaks);

        // Now generate slots. The time ranges which are blocked are marked as unavailable
        return slotService.generateSlots(begin, end, shop.getSlotConfig(), blockedSlots);
    }

    @Override
    @Transactional
    public int anonymizeExpired() {
        LocalDateTime until = clock.today().atStartOfDay();
        ZonedDateTime updatedTimestamp = clock.nowZoned();
        return reservationRepository.anonymizeExpired(until, updatedTimestamp);
    }

    @Override
    @Transactional
    public void createReservation(Shop shop, Slot.Id slotId, ContactType contactType, String contact, String name, String email) throws ReservationFailedException {
        LocalDateTime start = slotId.toLocalDateTime();
        LocalDateTime end = start.plusMinutes(shop.getSlotConfig().getTimePerSlot());

        // Check if that slot is a valid slot for the shop
        if (!slotService.isValidSlot(start, end, shop.getSlotConfig())) {
            throw new ReservationFailedException(String.format("Invalid slot [%s - %s]", start, end));
        }

        // Check if that slot is already reserved
        if (!reservationRepository.findReservationsForShop(shop.getId(), start, end).isEmpty()) {
            throw new ReservationFailedException(String.format("Slot [%s - %s] is already reserved", start, end));
        }

        // Check if the shop supports the contact type
        if (!shop.getContacts().containsKey(contactType)) {
            throw new ReservationFailedException(String.format("Shop doesn't support contact type %s", contactType));
        }

        Reservation.Id reservationId = Reservation.Id.random(uuidFactory);

        reservationRepository.insert(new Reservation(reservationId, shop.getId(), start, end, contact, email, name, contactType, false, clock.nowZoned(), clock.nowZoned()));

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

    /**
     * Takes a list of reservations and a list of breaks and generates the list of blocked slots.
     *
     * @param begin        start of the slots
     * @param end          end of the slots
     * @param reservations existing reservations
     * @param breaks       shop breaks
     * @return list of blocked slots
     */
    private List<Interval> blockSlots(LocalDate begin, LocalDate end, List<Reservation> reservations, Breaks breaks) {
        List<Interval> result = new ArrayList<>();

        // Mark all reservations as blocked
        for (Reservation reservation : reservations) {
            result.add(Interval.of(reservation.getStart(), reservation.getEnd()));
        }

        // Iterate through all days, find the breaks for that day and mark them as blocked
        LocalDate currentDate = begin;
        while (!currentDate.isAfter(end)) {
            for (Breaks.Break aBreak : breaks.at(currentDate.getDayOfWeek())) {
                result.add(Interval.of(
                    currentDate.atTime(aBreak.getStart()), currentDate.atTime(aBreak.getEnd())
                ));
            }

            currentDate = currentDate.plusDays(1);
        }

        return result;
    }
}
