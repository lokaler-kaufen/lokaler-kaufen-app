package de.qaware.mercury.mercury.business.email.impl;

import de.qaware.mercury.mercury.business.email.EmailSender;
import de.qaware.mercury.mercury.business.email.EmailService;
import de.qaware.mercury.mercury.business.email.SendEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
class EmailServiceImpl implements EmailService {
    private static final String SHOP_CREATION_SUBJECT = "Dein Laden auf lokaler.kaufen";
    private final EmailSender emailSender;

    EmailServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendShopCreationLink(String email) {
        String creationLink = "http://localhorst/"; // TODO MKA: Generate creation link
        String body = loadTemplate("/email/shop-creation.txt")
            .replace("{{ link }}", creationLink);

        emailSender.sendEmail(email, SHOP_CREATION_SUBJECT, body);
    }

    private String loadTemplate(String location) {
        try (InputStream stream = EmailServiceImpl.class.getResourceAsStream(location)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SendEmailException(String.format("Failed to read resource '%s'", location), e);
        }
    }
}
