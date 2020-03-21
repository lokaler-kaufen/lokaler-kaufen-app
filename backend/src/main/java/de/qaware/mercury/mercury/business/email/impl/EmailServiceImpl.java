package de.qaware.mercury.mercury.business.email.impl;

import de.qaware.mercury.mercury.business.email.EmailSender;
import de.qaware.mercury.mercury.business.email.EmailService;
import de.qaware.mercury.mercury.business.email.SendEmailException;
import de.qaware.mercury.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.mercury.business.login.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@EnableConfigurationProperties(EmailConfigurationProperties.class)
class EmailServiceImpl implements EmailService {
    private static final String SHOP_CREATION_SUBJECT = "Dein Laden auf lokaler.kaufen";
    private final EmailSender emailSender;
    private final EmailConfigurationProperties config;
    private final TokenService tokenService;

    EmailServiceImpl(EmailSender emailSender, EmailConfigurationProperties config, TokenService tokenService) {
        this.emailSender = emailSender;
        this.config = config;
        this.tokenService = tokenService;
    }

    @Override
    public void sendShopCreationLink(String email) {
        ShopCreationToken token = tokenService.createShopCreationToken(email);

        String creationLink = config.getCreationLinkTemplate()
            .replace("{{ token }}", token.getToken());
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
