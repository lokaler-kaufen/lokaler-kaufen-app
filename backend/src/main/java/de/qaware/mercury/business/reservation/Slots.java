package de.qaware.mercury.business.reservation;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

/**
 * Slots, grouped by date.
 */
@Value
@SuppressWarnings("java:S1700") // Shut up SonarQube
public class Slots {
    LocalDate start;
    LocalDate end;
    List<SlotDay> days;

    @Value
    public static class SlotDay {
        LocalDate date;
        List<Slot> slots;
    }
}
