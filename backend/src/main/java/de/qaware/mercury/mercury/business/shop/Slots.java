package de.qaware.mercury.mercury.business.shop;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class Slots {
    int timePerSlot;
    int timeBetweenSlots;

    @Nullable
    Slot monday;
    @Nullable
    Slot tuesday;
    @Nullable
    Slot wednesday;
    @Nullable
    Slot thursday;
    @Nullable
    Slot friday;
    @Nullable
    Slot saturday;
    @Nullable
    Slot sunday;

    public static Slots none(int timePerSlot, int timeBetweenSlots) {
        return new Slots(timePerSlot, timeBetweenSlots, null, null, null, null, null, null, null);
    }
}