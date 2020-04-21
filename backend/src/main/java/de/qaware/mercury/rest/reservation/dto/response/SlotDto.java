package de.qaware.mercury.rest.reservation.dto.response;

import de.qaware.mercury.business.reservation.Slot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDto {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private String id;
    private String start;
    private String end;
    private boolean available;

    public static SlotDto of(Slot slot) {
        return new SlotDto(
            slot.getId().getId(),
            slot.getStart().toLocalTime().format(TIME_FORMATTER),
            slot.getEnd().toLocalTime().format(TIME_FORMATTER),
            slot.isAvailable()
        );
    }
}
