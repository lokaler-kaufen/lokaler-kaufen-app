package de.qaware.mercury.business.time.impl


import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.time.impl.WallClock
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class ClockImplTest extends Specification {

    @Subject
    Clock clock = new WallClock()

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
