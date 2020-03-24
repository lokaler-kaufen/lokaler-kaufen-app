package de.qaware.mercury.mercury.business.email.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.qaware.mercury.mercury.business.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(EmailConfigurationProperties.class)
@Slf4j
@ConditionalOnProperty(name = "mercury.email.use-dummy", havingValue = "false")
class EmailSenderImpl implements EmailSender, AutoCloseable {
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

    @Override
    public void sendEmail(String recipient, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(config.getFrom());
        mail.setTo(recipient);
        mail.setSubject(subject);
        mail.setText(body);

        // Send email async
        executor.submit(() -> sendMailSafe(mail, recipient));
    }

    /**
     * Sends an email without throwing exceptions
     *
     * @param mail      mail to send
     * @param recipient recipient of the mail
     */
    private void sendMailSafe(SimpleMailMessage mail, String recipient) {
        log.debug("Sending mail to '{}'", recipient);
        try {
            mailSender.send(mail);
            log.debug("Sent mail to '{}'", recipient);
        } catch (Exception e) {
            log.error("Failed to send email to '{}', subject '{}'", recipient, mail.getSubject(), e);
        }
    }

    /**
     * Is called when Spring shuts down. Shuts down the executor.
     *
     * @throws Exception if something went wrong
     */
    @Override
    public void close() throws Exception {
        log.info("Shutting down email sender executor");
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        executor.shutdownNow();
    }
}
