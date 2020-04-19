package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.SlotConfig;

import java.time.LocalTime;

public final class SlotConfigFixtures {
    private SlotConfigFixtures() {
    }

    public static SlotConfig monday(int timePerSlot, int timeBetweenSlots, LocalTime start, LocalTime end) {
        return monday(timePerSlot, timeBetweenSlots, 0, start, end);
    }

    public static SlotConfig monday(int timePerSlot, int timeBetweenSlots, int delayForFirstSlot, LocalTime start, LocalTime end) {
        return new SlotConfig(timePerSlot, timeBetweenSlots, delayForFirstSlot, new DayConfig(start, end), null, null, null, null, null, null);
    }
}
