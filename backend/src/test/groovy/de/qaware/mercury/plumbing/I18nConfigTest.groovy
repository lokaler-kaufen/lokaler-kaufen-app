package de.qaware.mercury.plumbing

import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import spock.lang.Subject

class I18nConfigTest extends IntegrationTestSpecification {
    @Autowired
    @Subject
    MessageSource messageSource;

    def "key exists"() {
        expect:
        messageSource.getMessage("shop.creation.subject", null, LocaleContextHolder.getLocale()) == "Ihr Laden auf lokaler.kaufen"
    }

    def "key missing"() {
        when:
        messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())

        then:
        thrown(NoSuchMessageException)
    }

    def "unknown locale uses german"() {
        expect:
        messageSource.getMessage("shop.creation.subject", null, Locale.KOREAN) == "Ihr Laden auf lokaler.kaufen"
    }
}
