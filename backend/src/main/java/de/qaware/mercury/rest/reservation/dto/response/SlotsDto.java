package de.qaware.mercury.rest.reservation.dto.response;

import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.Slots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotsDto {
    /**
     * Groups slots by offset from a date, normally today.
     * <p>
     * Offset 0 then stores all slots today, offset 1 all slots tomorrow etc.
     */
    private Map<Long, List<SlotDto>> slots;

    public static SlotsDto of(Slots slots) {
        Map<Long, List<SlotDto>> result = new HashMap<>();
        for (long i = 0; i < slots.getDays(); i++) {
            result.put(i, new ArrayList<>());
        }

        for (Slot slot : slots.getSlots()) {
            // Calculate the offset from base date in days
            long offset = slots.getBegin().until(slot.getStart(), ChronoUnit.DAYS);
            // Add the slot in the list for that offset
            result.get(offset).add(SlotDto.of(slot));
        }

        return new SlotsDto(result);
    }
}
