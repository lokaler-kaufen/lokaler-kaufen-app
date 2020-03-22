package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.SlotConfig;
import de.qaware.mercury.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotConfigDto {
    private int timePerSlot;
    private int timeBetweenSlots;

    @Nullable
    private DayDto monday;
    @Nullable
    private DayDto tuesday;
    @Nullable
    private DayDto wednesday;
    @Nullable
    private DayDto thursday;
    @Nullable
    private DayDto friday;
    @Nullable
    private DayDto saturday;
    @Nullable
    private DayDto sunday;

    public SlotConfig toSlots() {
        return new SlotConfig(
            timePerSlot, timeBetweenSlots, Null.map(monday, DayDto::toSlot), Null.map(tuesday, DayDto::toSlot),
            Null.map(wednesday, DayDto::toSlot), Null.map(thursday, DayDto::toSlot), Null.map(friday, DayDto::toSlot),
            Null.map(saturday, DayDto::toSlot), Null.map(sunday, DayDto::toSlot)
        );
    }
}