package de.qaware.mercury.mercury.business.email.impl;

import de.qaware.mercury.mercury.business.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(EmailSenderConfigurationProperties.class)
@Slf4j
@ConditionalOnProperty(name = "mercury.email.use-dummy", havingValue = "false")
class EmailSenderImpl implements EmailSender {
    private final EmailSenderConfigurationProperties config;
    private final MailSender mailSender;

    EmailSenderImpl(EmailSenderConfigurationProperties config, MailSender mailSender) {
        this.config = config;
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(config.getFrom());
        mail.setTo(recipient);
        mail.setSubject(subject);
        mail.setText(body);

        log.debug("Sending email to '{}', subject '{}'", recipient, subject);
        mailSender.send(mail);
    }
}
