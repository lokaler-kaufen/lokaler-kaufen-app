package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.shop.SlotConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SlotService {

    /**
     * Generates a list of available time slots from a given slot config which are between the start and the end date.
     * If the start or end time of an existing reservation is within a time slot, it is omitted from the list.
     *
     * @param start        The start date
     * @param end          The end date, must be after start
     * @param slotConfig   The slot configuration
     * @param blockedSlots slots blocked by something, e.g. existing reservation or breaks
     * @return A list of available time slots between the start and end date.
     */
    Slots generateSlots(LocalDate start, LocalDate end, SlotConfig slotConfig, List<Interval> blockedSlots);

    /**
     * Checks if the given slot is a valid slot under the given slot configuration.
     *
     * @param start      start of the slot
     * @param end        end of the slot
     * @param slotConfig slot config
     * @return true if the slot is valid, false otherwise
     */
    boolean isValidSlot(LocalDateTime start, LocalDateTime end, SlotConfig slotConfig);

    Slots previewSlots(SlotConfig slotConfig);
}
