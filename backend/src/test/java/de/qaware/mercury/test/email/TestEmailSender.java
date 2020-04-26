package de.qaware.mercury.test.email;

import de.qaware.mercury.business.email.Attachment;
import de.qaware.mercury.business.email.EmailSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;

@Slf4j
public class TestEmailSender implements EmailSender {
    @Getter
    private final Emails emails = new Emails();

    @Override
    public void sendEmail(String recipient, String subject, String body, Attachment... attachments) {
        log.debug("Sending email to '{}', subject '{}', body '{}'", recipient, subject, body);
        emails.add(recipient, subject, body, attachments);
    }

    @Override
    public void sendEmails(List<String> recipients, String subject, String body, Attachment... attachments) {
        log.debug("Sending email to '{}', subject '{}', body '{}'", recipients, subject, body);
        emails.add(new HashSet<>(recipients), subject, body, attachments);
    }
}
