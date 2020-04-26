package de.qaware.mercury.business.email.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.qaware.mercury.business.email.Attachment;
import de.qaware.mercury.business.email.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
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
    private final JavaMailSender mailSender;
    private ExecutorService executor;

    EmailSenderImpl(EmailConfigurationProperties config, JavaMailSender mailSender) {
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
    public void sendEmail(String recipient, String subject, String body, Attachment... attachments) {
        sendEmails(Collections.singletonList(recipient), subject, body, attachments);
    }

    @Override
    public void sendEmails(List<String> recipients, String subject, String body, Attachment... attachments) {
        // Send email async
        executor.submit(() -> sendMailSafe(recipients, subject, body, attachments));
    }

    /**
     * Sends an email without throwing exceptions
     *
     * @param recipients recipient of the mail
     * @param subject    subject of the mail
     * @param body       body of the mail
     */
    private void sendMailSafe(List<String> recipients, String subject, String body, Attachment... attachments) {
        log.debug("Sending mail to '{}'", recipients);
        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
                mail.setFrom(config.getFrom());
                mail.setTo(recipients.toArray(new String[0]));
                mail.setSubject(subject);
                mail.setText(body);
                for (Attachment attachment : attachments) {
                    mail.addAttachment(attachment.getFilename(), new ByteArrayResource(attachment.getContent()), attachment.getContentType());
                }
            });
            log.debug("Sent mail to '{}'", recipients);
        } catch (Exception e) {
            log.error("Failed to send email to '{}', subject '{}'", recipients, subject, e);
        }
    }
}
