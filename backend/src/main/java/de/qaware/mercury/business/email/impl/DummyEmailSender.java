package de.qaware.mercury.business.email.impl;

import de.qaware.mercury.business.email.Attachment;
import de.qaware.mercury.business.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@ConditionalOnProperty(name = "mercury.email.use-dummy", havingValue = "true", matchIfMissing = true)
@Slf4j
class DummyEmailSender implements EmailSender {
    @Override
    public void sendEmail(String recipient, String subject, String body, Attachment... attachments) {
        sendEmails(Collections.singletonList(recipient), subject, body);
    }

    @Override
    public void sendEmails(List<String> recipients, String subject, String body, Attachment... attachments) {
        log.info("----------------------");
        log.info("Sending email(s) to {}", recipients);
        log.info("{}", subject);
        log.info("{}", body);
        log.info("----------------------");
    }
}
