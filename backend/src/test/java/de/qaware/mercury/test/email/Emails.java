package de.qaware.mercury.test.email;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Emails {
    @Getter
    private final List<Email> emails = new ArrayList<>();

    public void add(String recipient, String subject, String body) {
        add(Set.of(recipient), subject, body);
    }

    public void add(Set<String> recipients, String subject, String body) {
        add(new Email(recipients, subject, body));
    }

    public void add(Email email) {
        emails.add(email);
    }

    public Emails findWithRecipient(String recipient) {
        Emails result = new Emails();

        for (Email email : emails) {
            if (email.getRecipients().contains(recipient)) {
                result.add(email);
            }
        }

        return result;
    }

    public int size() {
        return emails.size();
    }

    public Email get(int index) {
        return emails.get(index);
    }
}
