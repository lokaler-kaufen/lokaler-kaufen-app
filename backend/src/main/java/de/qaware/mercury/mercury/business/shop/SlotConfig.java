package de.qaware.mercury.mercury.business.shop;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
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

    public static SlotConfig none(int timePerSlot, int timeBetweenSlots) {
        return new SlotConfig(timePerSlot, timeBetweenSlots, null, null, null, null, null, null, null);
    }
}