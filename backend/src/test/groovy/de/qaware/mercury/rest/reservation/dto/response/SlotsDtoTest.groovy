package de.qaware.mercury.rest.reservation.dto.response

import de.qaware.mercury.business.reservation.Slot
import de.qaware.mercury.business.reservation.Slots
import spock.lang.Specification

import java.time.LocalDate

class SlotsDtoTest extends Specification {
    def "offsets"() {
        setup:
        LocalDate today = LocalDate.of(2020, 1, 1)
        LocalDate tomorrow = today.plusDays(1)

        // Generated slots for 3 days
        Slots slots = new Slots(3, today, [
            // Slots for today
            new Slot(Slot.Id.of(today.atTime(1, 1)), today.atTime(1, 1), today.atTime(2, 2), true),
            new Slot(Slot.Id.of(today.atTime(3, 3)), today.atTime(3, 3), today.atTime(4, 4), true),
            // Slots for tomorrow
            new Slot(Slot.Id.of(tomorrow.atTime(1, 1)), tomorrow.atTime(1, 1), tomorrow.atTime(2, 2), true),
            new Slot(Slot.Id.of(tomorrow.atTime(3, 3)), tomorrow.atTime(3, 3), tomorrow.atTime(4, 4), true)
        ])

        when:
        SlotsDto dtos = SlotsDto.of(slots)

        then:
        // All offsets are there, even for the day after tomorrow
        dtos.slots.containsKey(0L)
        dtos.slots.containsKey(1L)
        dtos.slots.containsKey(2L)

        // Slots for today are put in offset 0
        dtos.slots[0L] == [
            new SlotDto("2020-01-01T01:01", "2020-01-01", "01:01", "02:02", true),
            new SlotDto("2020-01-01T03:03", "2020-01-01", "03:03", "04:04", true)
        ]

        // Slots for tomorrow are put in offset 1
        dtos.slots[1L] == [
            new SlotDto("2020-01-02T01:01", "2020-01-02", "01:01", "02:02", true),
            new SlotDto("2020-01-02T03:03", "2020-01-02", "03:03", "04:04", true)
        ]

        // Offset 2 (day after tomorrow) should be there, but is empty
        dtos.slots[2L].isEmpty()
    }
}
