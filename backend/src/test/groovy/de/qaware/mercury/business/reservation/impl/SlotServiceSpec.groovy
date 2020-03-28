package de.qaware.mercury.business.reservation.impl

import de.qaware.mercury.business.reservation.Interval
import de.qaware.mercury.business.reservation.Slot
import de.qaware.mercury.business.reservation.SlotService
import de.qaware.mercury.business.shop.DayConfig
import de.qaware.mercury.business.shop.SlotConfig
import de.qaware.mercury.business.time.Clock
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

import static de.qaware.mercury.business.shop.SlotConfig.builder
import static java.time.LocalDateTime.now

@ContextConfiguration(classes = SlotServiceImpl)
class SlotServiceSpec extends Specification {

    @Autowired
    SlotService slotService

    @SpringBean
    Clock clock = Mock()

    @Unroll
    def "Check Empty Slot Generation: #usecase"() {
        when:
        List<Slot> slots = slotService.generateSlots(start.toLocalDate(), end.toLocalDate(), config, [])

        then:
        slots.isEmpty()

        where:
        usecase         | start | end   | config
        'No Slot time'  | now() | now() | builder().timePerSlot(0).build()
        'No Day config' | now() | now() | builder().timePerSlot(1).build()
    }

    @Unroll
    def "Check Slot Generation: #usecase"() {
        when:
        List<Slot> slots = slotService.generateSlots(start, end, config, reservations)

        then:
        // Set the clock to 00:00 of the starting day to always get the same number of slots
        _ * clock.now() >> LocalDateTime.of(start, LocalTime.of(0, 0, 0))
        slots.size() == count

        where:
        usecase                   | start       | end         | config                            | reservations || count
        'Monday'                  | monday()    | monday()    | mondayConfig(10, 23)              | []           || 13
        'Tuesday'                 | tuesday()   | tuesday()   | tuesdayConfig(8, 18)              | []           || 10
        'Wednesday'               | wednesday() | wednesday() | wednesdayConfig(8, 18)            | []           || 10
        'Thursday'                | thursday()  | thursday()  | thursdayConfig(8, 18)             | []           || 10
        'Friday'                  | friday()    | friday()    | fridayConfig(8, 18)               | []           || 10
        'Saturday'                | saturday()  | saturday()  | saturdayConfig(8, 18)             | []           || 10
        'Sunday'                  | sunday()    | sunday()    | sundayConfig(8, 18)               | []           || 10
        'Monday til Wednesday'    | monday()    | wednesday() | mondayTilWednesday(8, 18)         | []           || 30
        'Monday (no pause)'       | monday()    | monday()    | mondayConfigNoPauses(8, 18)       | []           || 20

        // This is a special one. We don't want to get slots which have already started. Since we set the clock to
        // 00:00, the first available slot should not be part of the returned slot list.
        'Monday (after midnight)' | monday()    | monday()    | mondayConfig(0, 4)                | []           || 3

        // Another special one. Having an end time close to midnight (23:30) and a pause. This combination is a nice
        // edge case, to see if we are handling the end-of-day correctly.
        'Monday (til midnight)'   | monday()    | monday()    | mondayLate()                      | []           || 43

        // This is a special one. We don't want to get slots which have already started. Since we set the clock to
        // 00:00, the first available slot should not be part of the returned slot list.
        'Monday (after midnight)' | monday()    | monday()    | mondayPauseEndExactlyOnMidnight() | []           || 3
    }

    // The current calculation would return an empty list of slots if called with an start date after the end date.
    // To avoid silent errors due to mixed up dates, the method should throw an exception.
    def "Start dates must be before end dates."() {
        when:
        slotService.generateSlots(nextSaturday(), saturday(), saturdayConfig(8, 18), new ArrayList<Interval>())
        then:
        IllegalArgumentException _ = thrown()
    }

    static LocalDate monday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .toLocalDate()
    }

    static SlotConfig mondayConfig(int startHour, int endHour) {
        return defaultSlot()
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static SlotConfig mondayLate() {
        return builder()
            .timePerSlot(15)
            .timeBetweenSlots(5)
            .monday(new DayConfig(LocalTime.of(9, 00), LocalTime.of(23, 30)))
            .build()
    }

    static SlotConfig mondayPauseEndExactlyOnMidnight() {
        return builder()
            .timePerSlot(15)
            .timeBetweenSlots(5)
            .monday(new DayConfig(LocalTime.of(23, 00), LocalTime.of(23, 55)))
            .build()
    }

    static SlotConfig mondayConfigNoPauses(int startHour, int endHour) {
        return builder()
            .timePerSlot(30)
            .timeBetweenSlots(0)
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static LocalDate tuesday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY))
            .toLocalDate()
    }

    static SlotConfig tuesdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .tuesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static LocalDate wednesday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY))
            .toLocalDate()
    }

    static SlotConfig wednesdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .wednesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static LocalDate thursday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY))
            .toLocalDate()
    }

    static SlotConfig thursdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .thursday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static LocalDate friday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))
            .toLocalDate()
    }

    static SlotConfig fridayConfig(int startHour, int endHour) {
        return defaultSlot()
            .friday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static LocalDate saturday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
            .toLocalDate()
    }

    static LocalDate nextSaturday() {
        return now()
            .with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
            .toLocalDate()
    }

    static SlotConfig saturdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .saturday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static LocalDate sunday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .toLocalDate()
    }

    static SlotConfig sundayConfig(int startHour, int endHour) {
        return defaultSlot()
            .sunday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    static SlotConfig mondayTilWednesday(int startHour, int endHour) {
        return defaultSlot()
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .tuesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .wednesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    private static SlotConfig.SlotConfigBuilder defaultSlot() {
        return builder()
            .timePerSlot(30)
            .timeBetweenSlots(30)
    }
}
