package de.qaware.mercury.business.shop;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class SlotConfig {
    int timePerSlot;
    int timeBetweenSlots;
    int delayForFirstSlot;

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