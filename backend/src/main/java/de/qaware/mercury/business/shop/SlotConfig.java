package de.qaware.mercury.business.shop;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@AllArgsConstructor
public class SlotConfig {
    int timePerSlot;
    int timeBetweenSlots;

    @Nullable
    DayConfig monday;
    @Nullable
    DayConfig tuesday;
    @Nullable
    DayConfig wednesday;
    @Nullable
    DayConfig thursday;
    @Nullable
    DayConfig friday;
    @Nullable
    DayConfig saturday;
    @Nullable
    DayConfig sunday;
}