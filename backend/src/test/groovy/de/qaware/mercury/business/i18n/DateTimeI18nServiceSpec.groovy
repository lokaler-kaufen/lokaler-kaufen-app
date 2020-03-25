package de.qaware.mercury.business.i18n

import de.qaware.mercury.business.i18n.impl.DateTimeI18nServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@ContextConfiguration(classes = DateTimeI18nServiceImpl)
class DateTimeI18nServiceSpec extends Specification {

    @Autowired
    DateTimeI18nService dateTimeService

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
