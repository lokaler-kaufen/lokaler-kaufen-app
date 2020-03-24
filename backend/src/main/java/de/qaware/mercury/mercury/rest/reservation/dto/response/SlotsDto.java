package de.qaware.mercury.mercury.rest.reservation.dto.response;

import de.qaware.mercury.mercury.business.reservation.Slot;
import de.qaware.mercury.mercury.util.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotsDto {
    private List<SlotDto> slots;

    public static SlotsDto of(List<Slot> slots) {
        return new SlotsDto(Lists.map(slots, SlotDto::of));
    }
}
