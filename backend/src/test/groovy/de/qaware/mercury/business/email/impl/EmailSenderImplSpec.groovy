package de.qaware.mercury.business.email.impl

import org.springframework.mail.MailSender
import spock.lang.Specification

class EmailSenderImplSpec extends Specification {

    EmailConfigurationProperties properties
    MailSender mailSender
    EmailSenderImpl emailSender

    void setup() {
        properties = Mock(EmailConfigurationProperties)
        mailSender = Mock(MailSender)
        emailSender = new EmailSenderImpl(properties, mailSender)
        emailSender.init()
    }

    void cleanup() {
        emailSender.close()
    }

    def "Check successful mail sending"() {
        when:
        emailSender.sendEmail('mlr@qaware.de', 'Spock Tests', 'Rocks!')

        then:
        1 * properties.from >> 'test@lokaler.kaufen'
        noExceptionThrown()
        // 1 * mailSender.send(_)
    }
}
