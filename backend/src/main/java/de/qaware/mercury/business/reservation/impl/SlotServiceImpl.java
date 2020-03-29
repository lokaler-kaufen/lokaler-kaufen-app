package de.qaware.mercury.business.reservation.impl;

import de.qaware.mercury.business.reservation.Interval;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.business.time.Clock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SlotServiceImpl implements SlotService {
    private final Clock clock;

    @Override
    public List<Slot> generateSlots(LocalDate start, LocalDate end, SlotConfig slotConfig, List<Interval> existingReservations) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Start date must be before or same end date.");
        }
        // Safeguard against endless loops
        if (slotConfig.getTimePerSlot() < 1) {
            return List.of();
        }

        LocalDate current = start;

        List<Slot> result = new ArrayList<>();

        while (!current.isAfter(end)) {
            result.addAll(generateSlotsForDay(current, slotConfig, existingReservations));
            current = current.plusDays(1);
        }

        return result;
    }

    @Override
    public boolean isValidSlot(LocalDateTime start, LocalDateTime end, SlotConfig slotConfig) {
        // As slots don't go in the next day, it's safe to only generate slots for the start day
        List<Slot> slots = generateSlotsForDay(start.toLocalDate(), slotConfig, List.of());

        // And now check if the given slot is in the slot list
        for (Slot slot : slots) {
            if (slot.getStart().equals(start) && slot.getEnd().equals(end)) {
                return true;
            }
        }

        return false;
    }

    private List<Slot> generateSlotsForDay(LocalDate date, SlotConfig slotConfig, List<Interval> existingReservations) {
        DayConfig dayConfig = getDayConfig(date.getDayOfWeek(), slotConfig);
        if (dayConfig == null) {
            return List.of();
        }

        List<Slot> slots = new ArrayList<>();

        LocalTime currentStart = dayConfig.getStart();
        LocalDateTime now = clock.now();
        // while slot end <= end of opening hours && end of day
        while (isBeforeOrEqual(currentStart.plusMinutes(slotConfig.getTimePerSlot()), dayConfig.getEnd()) &&
            isBeforeMidnight(currentStart, slotConfig.getTimePerSlot())) {
            // start + length of slot
            LocalTime slotEnd = currentStart.plusMinutes(slotConfig.getTimePerSlot());
            Interval slot = Interval.of(date.atTime(currentStart), date.atTime(slotEnd));

            if (slot.getStart().isAfter(now)) {
                boolean available = checkAvailability(slot, existingReservations);
                slots.add(new Slot(Slot.Id.of(slot.getStart()), slot.getStart(), slot.getEnd(), available));
            }

            // Next start = end of slot + pause
            // but don't exceed 00:00 or we will loop endlessly
            if (!isBeforeMidnight(slotEnd, slotConfig.getTimeBetweenSlots())) {
                break;
            }
            currentStart = slotEnd.plusMinutes(slotConfig.getTimeBetweenSlots());
        }

        return slots;
    }

    /**
     * Compares time to otherTime and decides if otherTime is before or equal time.
     * This is necessary since isBefore excludes the equal case, unlike isAfter
     *
     * @param time      the reference time
     * @param otherTime the time to compare to the reference time
     */
    private boolean isBeforeOrEqual(LocalTime time, LocalTime otherTime) {
        return !time.isAfter(otherTime);
    }

    /***
     * Checks whether a given number of minutes will exceed 00:00 if added to a given time.
     * @param time the time
     * @param plusMinutes the number of minutes to add to time
     * @return true, if time + plusMinutes is after midnight
     */
    private boolean isBeforeMidnight(LocalTime time, int plusMinutes) {
        return time.getHour() * 60 + time.getMinute() + plusMinutes < 24 * 60;
    }

    private boolean checkAvailability(Interval slot, List<Interval> existingReservations) {
        for (Interval reservation : existingReservations) {
            if (slot.overlaps(reservation)) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    private DayConfig getDayConfig(DayOfWeek dayOfWeek, SlotConfig slotConfig) {
        switch (dayOfWeek) {
            case MONDAY:
                return slotConfig.getMonday();
            case TUESDAY:
                return slotConfig.getTuesday();
            case WEDNESDAY:
                return slotConfig.getWednesday();
            case THURSDAY:
                return slotConfig.getThursday();
            case FRIDAY:
                return slotConfig.getFriday();
            case SATURDAY:
                return slotConfig.getSaturday();
            case SUNDAY:
                return slotConfig.getSunday();
            default:
                throw new AssertionError("Unknown day of week " + dayOfWeek);
        }
    }
}
