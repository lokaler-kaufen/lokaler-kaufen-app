package de.qaware.mercury.mercury.business.reservation.impl;

import de.qaware.mercury.mercury.business.reservation.Interval;
import de.qaware.mercury.mercury.business.reservation.Slot;
import de.qaware.mercury.mercury.business.reservation.SlotService;
import de.qaware.mercury.mercury.business.shop.DayConfig;
import de.qaware.mercury.mercury.business.shop.SlotConfig;
import de.qaware.mercury.mercury.business.time.Clock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private List<Slot> generateSlotsForDay(LocalDate date, SlotConfig slotConfig, List<Interval> existingReservations) {
        DayConfig dayConfig = getDayConfig(date.getDayOfWeek(), slotConfig);
        if (dayConfig == null) {
            return List.of();
        }

        List<Slot> slots = new ArrayList<>();

        LocalTime currentStart = dayConfig.getStart();
        // while slot end <= end of day
        while (!currentStart.plusMinutes(slotConfig.getTimePerSlot()).isAfter(dayConfig.getEnd())) {
            // start + length of slot
            LocalTime slotEnd = currentStart.plusMinutes(slotConfig.getTimePerSlot());
            Interval slot = Interval.of(date.atTime(currentStart), date.atTime(slotEnd));

            if (hasAlreadyStarted(slot)) {
                continue;
            }

            boolean available = checkAvailability(slot, existingReservations);
            slots.add(new Slot(Slot.Id.of(slot.getStart()), slot.getStart(), slot.getEnd(), available));

            // Next start = end of slot + pause
            currentStart = slotEnd.plusMinutes(slotConfig.getTimeBetweenSlots());
        }

        return slots;
    }

    private boolean hasAlreadyStarted(Interval slot) {
        return slot.getStart().isBefore(clock.now());
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
