package de.qaware.mercury.business.time

import de.qaware.mercury.business.time.impl.WallClock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

@ContextConfiguration(classes = WallClock)
class ClockSpec extends Specification {

    @Autowired
    Clock clock

    def "Check Now Zoned"() {
        given:
        ZonedDateTime zonedNow = ZonedDateTime.now()

        when:
        ZonedDateTime zonedDateTime = clock.nowZoned()

        then:
        zonedDateTime
        with(zonedDateTime) {
            year == zonedNow.year
            month == zonedNow.month
            dayOfYear == zonedNow.dayOfYear
            hour == zonedNow.hour
            zone == zonedNow.zone
        }
    }

    def "Check Now"() {
        given:
        LocalDateTime now = LocalDateTime.now()

        when:
        LocalDateTime localDateTime = clock.now()

        then:
        localDateTime
        with(localDateTime) {
            year == now.year
            month == now.month
            dayOfYear == now.dayOfYear
            hour == now.hour
        }
    }

    def "Check Today"() {
        given:
        LocalDate today = LocalDateTime.now().toLocalDate()

        expect:
        clock.today() == today
    }
}
