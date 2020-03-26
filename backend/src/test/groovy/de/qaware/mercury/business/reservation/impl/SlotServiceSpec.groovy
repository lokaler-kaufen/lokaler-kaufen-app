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
        _ * clock.now() >> start
        slots.size() == count

        where:
        usecase                 | start       | end         | config                    | reservations || count
        'Monday'                | monday()    | monday()    | mondayConfig(10, 23)      | []           || 13
        'Tuesday'               | tuesday()   | tuesday()   | tuesdayConfig(8, 18)      | []           || 10
        'Wednesday'             | wednesday() | wednesday() | wednesdayConfig(8, 18)    | []           || 10
        'Thursday'              | thursday()  | thursday()  | thursdayConfig(8, 18)     | []           || 10
        'Friday'                | friday()    | friday()    | fridayConfig(8, 18)       | []           || 10
        'Saturday'              | saturday()  | saturday()  | saturdayConfig(8, 18)     | []           || 10
        'Sunday'                | sunday()    | sunday()    | sundayConfig(8, 18)       | []           || 10
        'Monday til Wednesday'  | monday()    | wednesday() | mondayTilWednesday(8, 18) | []           || 30
        'Saturday til Tuesday ' | saturday()  | tuesday()   | saturdayTilTuesday(8, 18) | []           || 30
        'Monday'                | monday()    | monday()    | mondayConfig(10, 14)      | []           || 4

    }

    LocalDateTime monday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

    SlotConfig mondayConfig(int startHour, int endHour) {
        return defaultSlot()
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime tuesday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY))
    }

    SlotConfig tuesdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .tuesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime wednesday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY))
    }

    SlotConfig wednesdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .wednesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime thursday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY))
    }

    SlotConfig thursdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .thursday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime friday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))
    }

    SlotConfig fridayConfig(int startHour, int endHour) {
        return defaultSlot()
            .friday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime saturday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
    }

    SlotConfig saturdayConfig(int startHour, int endHour) {
        return defaultSlot()
            .saturday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    LocalDateTime sunday() {
        return now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    }

    SlotConfig sundayConfig(int startHour, int endHour) {
        return defaultSlot()
            .sunday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    SlotConfig mondayTilWednesday(int startHour, int endHour) {
        return defaultSlot()
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .tuesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .wednesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    SlotConfig saturdayTilTuesday(int startHour, int endHour) {
        return defaultSlot()
            .saturday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .monday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .tuesday(new DayConfig(LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)))
            .build()
    }

    private SlotConfig.SlotConfigBuilder defaultSlot() {
        return builder()
            .timePerSlot(30)
            .timeBetweenSlots(30)
    }
}
