package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.shop.SlotConfig;

import java.time.LocalDate;
import java.util.List;

public interface SlotService {
    List<Slot> generateSlots(LocalDate start, LocalDate end, SlotConfig slotConfig, List<Interval> existingReservations);
}
