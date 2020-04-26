package de.qaware.mercury.business.email;

import lombok.Value;

/**
 * An email attachment.
 */
@Value
public class Attachment {
    String filename;
    String contentType;
    byte[] content;
}
