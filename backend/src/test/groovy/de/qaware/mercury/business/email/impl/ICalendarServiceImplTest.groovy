package de.qaware.mercury.business.email.impl

import de.qaware.mercury.business.email.ICalendarService
import de.qaware.mercury.business.reservation.Reservation
import de.qaware.mercury.test.time.TestClock
import spock.lang.Specification
import spock.lang.Subject

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

class ICalendarServiceImplTest extends Specification {
    @Subject
    ICalendarService sut
    TestClock testClock = TestClock.fixed()

    def setup() {
        ICalendarConfigurationProperties configuration = new ICalendarConfigurationProperties()
        configuration.setOrganizerEmail("organizer@local.host")
        configuration.setOrganizerName("The organizer")

        sut = new ICalendarServiceImpl(testClock, configuration)
    }

    def "create"() {
        given:
        Reservation.Id reservationId = Reservation.Id.of(UUID.fromString("830d9f5c-28c4-4bdf-b5d2-22f57c49bd64"))
        LocalDateTime start = LocalDateTime.of(2020, 4, 26, 17, 0)
        LocalDateTime end = LocalDateTime.of(2020, 4, 26, 19, 0)

        when:
        String ics = sut.newReservation(reservationId, start, end, "This is the summary", "This is the description\nAnd it contains a new line\r\nand several chars: , ; \\")

        then:
        // Compare ignoring the line endings
        ics.readLines() == ICalendarServiceImplTest.getResourceAsStream("/icalendar/1.ics").readLines(StandardCharsets.UTF_8.toString())
    }
}
