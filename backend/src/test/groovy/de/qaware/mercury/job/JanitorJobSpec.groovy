package de.qaware.mercury.job

import de.qaware.mercury.business.reservation.ReservationService
import spock.lang.Specification
import spock.lang.Subject

class JanitorJobSpec extends Specification {

    @Subject
    private JanitorJob janitorJob

    private ReservationService reservationService

    def setup() {
        reservationService = Mock(ReservationService)
        janitorJob = new JanitorJob(reservationService)
    }

    def "Test calls ReservationService"() {
        when:
        janitorJob.cleanReservations()

        then:
        1 * reservationService.anonymizeExpired()
    }

}
