package de.qaware.mercury.business.email;

import de.qaware.mercury.business.TechnicalException;

public class SendEmailException extends TechnicalException {
    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
