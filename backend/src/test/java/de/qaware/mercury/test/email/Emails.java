package de.qaware.mercury.test.email;

import de.qaware.mercury.business.email.Attachment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Emails {
    @Getter
    private final List<Email> emails = new ArrayList<>();

    public void add(String recipient, String subject, String body, Attachment... attachments) {
        add(Set.of(recipient), subject, body, attachments);
    }

    public void add(Set<String> recipients, String subject, String body, Attachment... attachments) {
        add(new Email(recipients, subject, body, Arrays.asList(attachments)));
    }

    public void add(Email email) {
        emails.add(email);
    }

    public Emails findWithRecipient(String recipient) {
        return filterEmails(email -> email.getRecipients().contains(recipient));
    }

    public Emails findWithSubjectContaining(String text) {
        return filterEmails(email -> email.getSubject().contains(text));
    }

    public int size() {
        return emails.size();
    }

    public Email get(int index) {
        return emails.get(index);
    }

    public void clear() {
        emails.clear();
    }

    private Emails filterEmails(Predicate<Email> predicate) {
        Emails result = new Emails();

        for (Email email : emails) {
            if (predicate.test(email)) {
                result.add(email);
            }
        }

        return result;
    }
}
