package de.qaware.mercury.business.reservation.impl

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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoField
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
        List<Slot> slots = slotService.generateSlots(start.toLocalDate(), end.toLocalDate(), config, reservations)

        then:
        1 * clock.now() >> start
        slots.size() == count

        where:
        usecase     | start        | end           | config                 | reservations || count
        'Monday'    | monday(8)    | monday(18)    | mondayConfig(8, 18)    | []           || 10

        // TODO Check and fix these tests
        // 'Monday Reserved'  | monday(8)    | monday(18)    | mondayConfig(8, 18)    | [of(monday(8), monday(18))] || 0
        // 'Monday Too Early' | monday(8)    | monday(10)    | mondayConfig(10, 14)   | []                          || 0
        // 'Monday Too Late'  | monday(16)   | monday(18)    | mondayConfig(10, 14)   | []                          || 0

        'Tuesday'   | tuesday(8)   | tuesday(18)   | tuesdayConfig(8, 18)   | []           || 10
        'Wednesday' | wednesday(8) | wednesday(18) | wednesdayConfig(8, 18) | []           || 10
        'Thursday'  | thursday(8)  | thursday(18)  | thursdayConfig(8, 18)  | []           || 10
        'Friday'    | friday(8)    | friday(18)    | fridayConfig(8, 18)    | []           || 10
        'Saturday'  | saturday(8)  | saturday(18)  | saturdayConfig(8, 18)  | []           || 10
        'Sunday'    | sunday(8)    | sunday(18)    | sundayConfig(8, 18)    | []           || 10
    }

    LocalDateTime monday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig mondayConfig(int startHour, int endHour) {
        return defaultSlot()
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime tuesday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig tuesdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .tuesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime wednesday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig wednesdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .wednesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime thursday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig thursdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .thursday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime friday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig fridayConfig(int startHour, int endHour) {
        return defaultSlot()
            .friday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime saturday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig saturdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .saturday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime sunday(int hourOfDay) {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .with(ChronoField.HOUR_OF_DAY, hourOfDay)
            .with(ChronoField.SECOND_OF_DAY, 0)
            .with(ChronoField.MILLI_OF_DAY, 0)
            .with(ChronoField.MICRO_OF_DAY, 0)
    }

    SlotConfig sundayConfig(int startHour, int endHour) {
        return defaultSlot()
            .sunday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    private SlotConfig.SlotConfigBuilder defaultSlot() {
        return builder()
            .timePerSlot(30)
            .timeBetweenSlots(30)
    }
}
