package de.qaware.mercury.business.login;

import de.qaware.mercury.business.TechnicalException;

public class TokenTechnicalException extends TechnicalException {
    public TokenTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
