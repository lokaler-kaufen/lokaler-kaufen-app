package de.qaware.mercury.business.email;

public interface EmailSender {
    void sendEmail(String recipient, String subject, String body);
}
