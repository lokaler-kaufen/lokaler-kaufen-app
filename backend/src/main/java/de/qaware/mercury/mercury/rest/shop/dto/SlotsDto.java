package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Slots;
import de.qaware.mercury.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotsDto {
    private int timePerSlot;
    private int timeBetweenSlots;

    @Nullable
    private SlotDto monday;
    @Nullable
    private SlotDto tuesday;
    @Nullable
    private SlotDto wednesday;
    @Nullable
    private SlotDto thursday;
    @Nullable
    private SlotDto friday;
    @Nullable
    private SlotDto saturday;
    @Nullable
    private SlotDto sunday;

    public Slots toSlots() {
        return new Slots(
            timePerSlot, timeBetweenSlots, Null.map(monday, SlotDto::toSlot), Null.map(tuesday, SlotDto::toSlot),
            Null.map(wednesday, SlotDto::toSlot), Null.map(thursday, SlotDto::toSlot), Null.map(friday, SlotDto::toSlot),
            Null.map(saturday, SlotDto::toSlot), Null.map(sunday, SlotDto::toSlot)
        );
    }
}