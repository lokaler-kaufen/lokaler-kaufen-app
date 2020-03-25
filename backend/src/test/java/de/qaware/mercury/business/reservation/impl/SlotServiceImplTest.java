package de.qaware.mercury.business.reservation.impl;

import de.qaware.mercury.business.reservation.Interval;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.business.time.impl.WallClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Disabled
class SlotServiceImplTest {
    private SlotServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new SlotServiceImpl(new WallClock());
    }

    @Test
    void test() {
        SlotConfig slotConfig = new SlotConfig(55, 5,
            new DayConfig(LocalTime.of(8, 0), LocalTime.of(15, 0)),
            new DayConfig(LocalTime.of(10, 0), LocalTime.of(20, 0)),
            new DayConfig(LocalTime.of(9, 0), LocalTime.of(17, 0)),
            new DayConfig(LocalTime.of(12, 0), LocalTime.of(16, 0)),
            new DayConfig(LocalTime.of(8, 0), LocalTime.of(12, 0)),
            null,
            null
        );
        List<Slot> slots = sut.generateSlots(LocalDate.of(2020, 3, 16), LocalDate.of(2020, 3, 22), slotConfig, List.of(
            Interval.of(LocalDateTime.of(2020, 3, 16, 8, 30), LocalDateTime.of(2020, 3, 16, 10, 0))
        ));

        for (Slot slot : slots) {
            System.out.println(slot);
        }
    }

}