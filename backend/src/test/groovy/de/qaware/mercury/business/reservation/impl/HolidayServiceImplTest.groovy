package de.qaware.mercury.business.reservation.impl

import de.qaware.mercury.business.location.FederalState
import de.qaware.mercury.business.reservation.HolidayService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class HolidayServiceImplTest extends Specification {
    @Shared
    @Subject
    HolidayService sut = new HolidayServiceImpl();

    def "bavarian holidays"() {
        expect:
        sut.isHoliday(date, FederalState.BAVARIA) == result

        where:
        date                       | result
        LocalDate.of(2020, 1, 1)   | true
        LocalDate.of(2020, 1, 6)   | true
        LocalDate.of(2020, 4, 10)  | true
        LocalDate.of(2020, 4, 13)  | true
        LocalDate.of(2020, 5, 1)   | true
        LocalDate.of(2020, 5, 21)  | true
        LocalDate.of(2020, 6, 1)   | true
        LocalDate.of(2020, 6, 11)  | true
        LocalDate.of(2020, 8, 15)  | true
        LocalDate.of(2020, 10, 3)  | true
        LocalDate.of(2020, 11, 1)  | true
        LocalDate.of(2020, 12, 25) | true
        LocalDate.of(2020, 12, 26) | true
        LocalDate.of(2020, 7, 17)  | false
    }
}
