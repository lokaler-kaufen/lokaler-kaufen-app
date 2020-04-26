package de.qaware.mercury.business.email.impl


import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification
import spock.lang.Subject

class EmailSenderImplSpec extends Specification {

    EmailConfigurationProperties properties = new EmailConfigurationProperties()
    JavaMailSender mailSender = Mock()
    @Subject
    EmailSenderImpl emailSender

    void setup() {
        properties.setFrom("test@local.host")

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
        noExceptionThrown()
    }
}
