package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.SlotConfig;
import de.qaware.mercury.mercury.util.Null;
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

    public SlotConfig toSlots() {
        return new SlotConfig(
            timePerSlot, timeBetweenSlots, Null.map(monday, DayDto::toSlot), Null.map(tuesday, DayDto::toSlot),
            Null.map(wednesday, DayDto::toSlot), Null.map(thursday, DayDto::toSlot), Null.map(friday, DayDto::toSlot),
            Null.map(saturday, DayDto::toSlot), Null.map(sunday, DayDto::toSlot)
        );
    }
}