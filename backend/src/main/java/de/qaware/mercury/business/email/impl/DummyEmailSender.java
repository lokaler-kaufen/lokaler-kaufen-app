package de.qaware.mercury.business.email.impl;

import de.qaware.mercury.business.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "mercury.email.use-dummy", havingValue = "true", matchIfMissing = true)
@Slf4j
class DummyEmailSender implements EmailSender {
    @Override
    public void sendEmail(String recipient, String subject, String body) {
        log.info("----------------------");
        log.info("Sending email to {}", recipient);
        log.info("{}", subject);
        log.info("{}", body);
        log.info("----------------------");
    }
}
