package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreaksDto {
    @NotNull
    private Set<String> slotIds;

    public Set<Slot.Id> toSlotIds() {
        return Sets.map(slotIds, Slot.Id::parse);
    }

    public static BreaksDto fromSlots(List<Slot> slots) {
        return new BreaksDto(Sets.map(slots, s -> s.getId().getId()));
    }
}
