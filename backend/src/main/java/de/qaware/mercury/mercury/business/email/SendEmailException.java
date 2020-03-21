package de.qaware.mercury.mercury.business.email;

import de.qaware.mercury.mercury.business.TechnicalException;

public class SendEmailException extends TechnicalException {
    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
