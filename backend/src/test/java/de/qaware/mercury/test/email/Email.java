package de.qaware.mercury.test.email;

import de.qaware.mercury.business.email.Attachment;
import lombok.Value;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
public class Email {
    public static final Pattern TOKEN_PATTERN = Pattern.compile("token=(.*?)\\s");

    Set<String> recipients;
    String subject;
    String body;
    List<Attachment> attachments;

    /**
     * Searches for a token in the body.
     */
    public String findToken() {
        Matcher matcher = TOKEN_PATTERN.matcher(body);
        if (!matcher.find()) {
            throw new NoSuchElementException(String.format("No token found. Body: '%s'", body));
        }

        return matcher.group(1);
    }
}