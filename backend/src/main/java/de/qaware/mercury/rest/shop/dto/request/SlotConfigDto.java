package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotConfigDto {
    @Min(1)
    private int timePerSlot;
    @Min(0)
    private int timeBetweenSlots;

    @Nullable
    @Valid
    private DayDto monday;
    @Nullable
    @Valid
    private DayDto tuesday;
    @Nullable
    @Valid
    private DayDto wednesday;
    @Nullable
    @Valid
    private DayDto thursday;
    @Nullable
    @Valid
    private DayDto friday;
    @Nullable
    @Valid
    private DayDto saturday;
    @Nullable
    @Valid
    private DayDto sunday;

    public static SlotConfigDto of(SlotConfig slotConfig) {
        return new SlotConfigDto(
            slotConfig.getTimePerSlot(),
            slotConfig.getTimeBetweenSlots(),
            Null.map(slotConfig.getMonday(), DayDto::of),
            Null.map(slotConfig.getTuesday(), DayDto::of),
            Null.map(slotConfig.getWednesday(), DayDto::of),
            Null.map(slotConfig.getThursday(), DayDto::of),
            Null.map(slotConfig.getFriday(), DayDto::of),
            Null.map(slotConfig.getSaturday(), DayDto::of),
            Null.map(slotConfig.getSunday(), DayDto::of)
        );
    }

    public SlotConfig toSlots() {
        return new SlotConfig(
            timePerSlot, timeBetweenSlots, Null.map(monday, DayDto::toSlot), Null.map(tuesday, DayDto::toSlot),
            Null.map(wednesday, DayDto::toSlot), Null.map(thursday, DayDto::toSlot), Null.map(friday, DayDto::toSlot),
            Null.map(saturday, DayDto::toSlot), Null.map(sunday, DayDto::toSlot)
        );
    }
}