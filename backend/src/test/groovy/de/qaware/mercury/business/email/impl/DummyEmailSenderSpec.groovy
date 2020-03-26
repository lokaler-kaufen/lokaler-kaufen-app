package de.qaware.mercury.business.email.impl

import spock.lang.Specification

class DummyEmailSenderSpec extends Specification {
    def "Check Dummy Email sender"() {
        setup:
        DummyEmailSender sender = new DummyEmailSender()

        when:
        sender.sendEmail('test@lokaler.kaufen', 'Test Subject', 'Hello Spock')

        then:
        noExceptionThrown()
    }
}
