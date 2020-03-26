package de.qaware.mercury.rest.shop.dto.request

import de.qaware.mercury.business.shop.DayConfig
import de.qaware.mercury.rest.shop.InvalidTimeException
import spock.lang.Specification

import java.time.LocalTime

class DayDtoSpec extends Specification {

    def "Check Slot for DayDto"() {
        given:
        DayConfig dayConfig = new DayDto("10:00", "23:59").toSlot()

        expect:
        dayConfig.getStart() == LocalTime.of(10, 0)
        dayConfig.getEnd() == LocalTime.of(23, 59)
    }

    def "Invalid Time for Slot"() {
        given:
        DayDto dto = new DayDto("foobar", "23:59")

        when:
        dto.toSlot()

        then:
        InvalidTimeException ex = thrown()
        ex.message.contains('foobar')
    }
}
