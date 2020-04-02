package de.qaware.mercury.business.email.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.qaware.mercury.business.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(EmailConfigurationProperties.class)
@Slf4j
@ConditionalOnProperty(name = "mercury.email.use-dummy", havingValue = "false")
class EmailSenderImpl implements EmailSender {
    private final EmailConfigurationProperties config;
    private final MailSender mailSender;
    private ExecutorService executor;

    EmailSenderImpl(EmailConfigurationProperties config, MailSender mailSender) {
        this.config = config;
        this.mailSender = mailSender;
    }

    @PostConstruct
    void init() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("email-sender-%d")
            .setDaemon(true)
            .build();

        log.info("Starting email sender executor");
        executor = Executors.newSingleThreadExecutor(threadFactory);
    }

    /**
     * Is called when Spring shuts down. Shuts down the executor.
     */
    @PreDestroy
    void close() {
        log.info("Shutting down email sender executor");
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdownNow();
    }

    @Override
    public void sendEmail(String recipient, String subject, String body) {
        sendEmails(Collections.singletonList(recipient), subject, body);
    }

    @Override
    public void sendEmails(List<String> recipients, String subject, String body) {
        String[] recipientArray = recipients.toArray(new String[0]);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(config.getFrom());
        mail.setTo(recipientArray);
        mail.setSubject(subject);
        mail.setText(body);

        // Send email async
        executor.submit(() -> sendMailSafe(mail, recipientArray));
    }

    /**
     * Sends an email without throwing exceptions
     *
     * @param mail       mail to send
     * @param recipients recipient of the mail
     */
    private void sendMailSafe(SimpleMailMessage mail, String... recipients) {
        String recipientsAsString = Arrays.toString(recipients);

        log.debug("Sending mail to '{}'", recipientsAsString);
        try {
            mailSender.send(mail);
            log.debug("Sent mail to '{}'", recipientsAsString);
        } catch (Exception e) {
            log.error("Failed to send email to '{}', subject '{}'", recipientsAsString, mail.getSubject(), e);
        }
    }
}
