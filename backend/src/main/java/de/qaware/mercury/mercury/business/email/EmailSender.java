package de.qaware.mercury.mercury.business.email;

public interface EmailSender {
    void sendEmail(String recipient, String subject, String body);
}
