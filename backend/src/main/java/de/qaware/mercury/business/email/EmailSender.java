package de.qaware.mercury.business.email;

import java.util.List;

public interface EmailSender {
    void sendEmail(String recipient, String subject, String body, Attachment... attachments);

    void sendEmails(List<String> recipients, String subject, String body, Attachment... attachments);
}
