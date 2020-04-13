package de.qaware.mercury.business.i18n.impl

import de.qaware.mercury.business.i18n.DateTimeI18nService
import de.qaware.mercury.business.i18n.impl.DateTimeI18nServiceImpl
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class DateTimeI18nServiceImplTest extends Specification {

    @Subject
    DateTimeI18nService dateTimeService = new DateTimeI18nServiceImpl()

    LocalDateTime localDateTime

    void setup() {
        localDateTime = LocalDateTime.now()
    }

    def "Check FormatDate with LocalDateTime"() {
        expect:
        dateTimeService.formatDate(localDateTime)
    }

    def "Check FormatDate with LocalDate"() {
        expect:
        dateTimeService.formatDate(localDateTime.toLocalDate())
    }

    def "Check FormatTime with LocalDateTime"() {
        expect:
        dateTimeService.formatTime(localDateTime)
    }

    def "Check FormatTime with Local    Time"() {
        expect:
        dateTimeService.formatTime(localDateTime.toLocalTime())
    }
}
